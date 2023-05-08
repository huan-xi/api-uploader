package cn.hperfect.apikit.service.platform.yapi;

import cn.hperfect.apikit.cons.SysCons;
import cn.hperfect.apikit.cons.Yapis;
import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.core.model.ApiModel;
import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.core.model.type.ApiEnumPropNode;
import cn.hperfect.apikit.core.model.type.ApiRefPropNode;
import cn.hperfect.apikit.enums.ApiParamType;
import cn.hperfect.apikit.service.platform.yapi.dto.*;
import cn.hperfect.apikit.settings.AppSettingsState;
import cn.hperfect.apikit.utils.ClipboardUtil;
import cn.hperfect.apikit.utils.JsonUtil;
import cn.hperfect.apikit.utils.NotificationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.google.common.reflect.TypeToken;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public class YapiUploadService {

    private final AppSettingsState settings;

    private final Project project;
    private final YapiClient yapiClient;

    public YapiUploadService(Project project) {
        this.settings = AppSettingsState.getInstance(project);
        this.project = project;
        this.yapiClient = new YapiClient(settings);
    }

    public void uploadAsync(Project project, ApiCat apiCat) {
        // 异步处理
        ProgressManager.getInstance().run(new Task.Backgroundable(project, SysCons.NAME) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                try {
                    upload(apiCat);
                } catch (Exception exception) {
                    NotificationUtils.notifyError(project, "上传接口错误:" + exception.getMessage());
                }

            }
        });
    }

    public void upload(ApiCat apiCat) {
        if (settings == null||!settings.validateYapi()) {
            NotificationUtils.notifyError(project, "请在 settings -> tools -> api-uploader configuration 中配置相关属性");
            return;
        }
//        System.out.println("apiCat:" + Hex.encodeHexString(SerializeUtil.serialize(apiCat)));
        //创建或获取分类
        Integer catId = this.getOrCreateCat(apiCat.getCatName(), apiCat.getCatDesc());
        if (catId == null) {
            NotificationUtils.notifyError(project, "创建分类失败");
            return;
        }
        //上传api
        List<ApiModel> apis = apiCat.getApiModels();
        int success = 0;
        Integer lastId = null;
        if (CollUtil.isNotEmpty(apis)) {
            //创建api
            for (ApiModel api : apis) {
                YapiApiDTO yapiApiDto = getYapiApiDTO(catId, api);

                //query参数
                List<BaseApiPropNode> query = api.getQuery();
                yapiApiDto.setReqQuery(toQueryParamDTOs(query));
                BaseApiPropNode resultType = api.getResultType();
                if (resultType != null) {
                    yapiApiDto.setResBody(JsonUtil.toJson(nodeToBodyDTO(resultType)));
                }
                yapiApiDto.setReqBodyOther(JsonUtil.toJson(nodeToBodyDTO(api.getBody())));
                lastId = this.saveApi(yapiApiDto);
                if (lastId != null) {
                    success++;
                }
            }
        }
        if (apis != null && apis.size() == 1 && lastId == null) {
            //第二次上传获取id, 服务端新增不会返回id
            YapiApiDTO yapiApiDto = getYapiApiDTO(catId, apis.get(0));
            lastId = this.saveApi(yapiApiDto);
            if (lastId == null) {
                NotificationUtils.notifyError(project, "上传失败,未知原因");
            }
        }
        NotificationUtils.info(project, String.format("总共解析接口:%d个,成功上传:%d个,api已复制到剪切板", apis.size(), success));
        if (lastId != null) {
            ClipboardUtil.setStr(String.format("%s/project/%s/interface/api/%s", settings.getYapiHost(), settings.getYapiProjectId(), lastId));
        } else if (catId != null) {
            ClipboardUtil.setStr(String.format("%s/project/%s/interface/api/cat_%s", settings.getYapiHost(), settings.getYapiProjectId(), catId));
        }
    }

    private YapiApiDTO getYapiApiDTO(Integer catId, ApiModel api) {
        YapiApiDTO yapiApiDto = new YapiApiDTO()
                .setCatId(catId)
                .setToken(settings.getYapiToken())
                .setMethod(api.getApiType().name())
                .setTitle(api.getName())
                .setPath(api.getPath())
                .setDesc(api.getDesc());
        return yapiApiDto;
    }

    public Integer saveApi(YapiApiDTO yapiApiDto) {
        yapiApiDto.setToken(settings.getYapiToken());
        String resp = yapiClient.postJson(Yapis.SAVE, yapiApiDto);
        if (YapiResponse.isSuccess(resp)) {
            Type type = new TypeToken<YapiResponse<List<SaveApiDTO>>>() {
            }.getType();
            YapiResponse<List<SaveApiDTO>> vos = JsonUtil.fromJson(resp, type);
            if (CollUtil.isNotEmpty(vos.getData())) {
                return vos.getData().get(0).getId();
            }
        } else {
            Type type = new TypeToken<YapiResponse<String>>() {
            }.getType();
            YapiResponse<String> res = JsonUtil.fromJson(resp, type);
            throw new RuntimeException("yapi 上传失败:" + res.getErrmsg());

        }
        return null;
    }

    public void listCat() {

    }


    /**
     * 转换成yapi 的类型
     *
     * @param paramModelNode
     * @return
     */

    private String toYapiType(BaseApiPropNode paramModelNode) {
        ApiParamType type = paramModelNode.getType();
        String yapi = YapiTypeMap.MAP.get(type);
        Assert.notBlank(yapi, "未找到对应类型的yapi类型:" + type.name());
        return yapi;
    }

    private List<QueryItemDTO> toQueryParamDTOs(List<BaseApiPropNode> formParams) {
        List<QueryItemDTO> queryItemDtoList = new ArrayList<>();
        for (BaseApiPropNode formParam : formParams) {
            List<BaseApiPropNode> paramModelList = formParam.getParamModelList();
            //转化成yapi才是
            if (CollUtil.isNotEmpty(paramModelList)) {
                queryItemDtoList.addAll(toQueryParamDTOs(paramModelList));
            } else if (StrUtil.isNotBlank(formParam.getName())) {
                queryItemDtoList.add(nodeToQueryDTO(formParam));
            }
        }
        return queryItemDtoList;
    }

    /**
     * 转换成body参数
     *
     * @param paramModelNode
     * @return
     */
    private YapiTypeDTO nodeToBodyDTO(BaseApiPropNode paramModelNode) {
        YapiTypeDTO yapiTypeDto = new YapiTypeDTO();
        if (paramModelNode == null) {
            return yapiTypeDto;
        }
        String type = toYapiType(paramModelNode);
        yapiTypeDto.setType(type);
        yapiTypeDto.setTitle(paramModelNode.getName());
        if (StrUtil.isNotBlank(paramModelNode.getExample())) {
            yapiTypeDto.setMock(new YapiTypeDTO.Mock(paramModelNode.getExample()));
        }
        if (StrUtil.isNotBlank(paramModelNode.getMockExpr())) {
            yapiTypeDto.setMock(new YapiTypeDTO.Mock(paramModelNode.getMockExpr()));
        }
       /* if (type != "object") {
            //object 设置会报错 yapi的bug
            yapiTypeDto.setRequired(paramModelNode.isRequired() ? "1" : "0");
        }*/

        yapiTypeDto.setDescription(paramModelNode.getDesc());

        if (paramModelNode.getType() == ApiParamType.LIST) {
            //list类型
            if (CollUtil.isNotEmpty(paramModelNode.getParamModelList())) {
                yapiTypeDto.setItems(nodeToBodyDTO(paramModelNode.getParamModelList().get(0)));
            } else {
                //位置子元素类型
                YapiTypeDTO dto = new YapiTypeDTO();
                dto.setType("object");
                yapiTypeDto.setItems(dto);
            }
            return yapiTypeDto;
        }
        if (paramModelNode instanceof ApiEnumPropNode) {
            ApiEnumPropNode enumPropNode = (ApiEnumPropNode) paramModelNode;
            List<ApiEnumPropNode.EnumValue> values = enumPropNode.getValues();
            List<String> names = values.stream().map(ApiEnumPropNode.EnumValue::getName).collect(Collectors.toList());
            yapiTypeDto.setEnumList(names);
            yapiTypeDto.setEnumDesc(enumPropNode.parseEnumDesc());
        }


        List<BaseApiPropNode> paramModelList = paramModelNode.getParamModelList();
        if (CollUtil.isNotEmpty(paramModelList)) {
            Map<String, YapiTypeDTO> dtoMap = new HashMap<>();
            //子属性
            for (BaseApiPropNode apiParamModelNode : paramModelList) {
                if (StrUtil.isNotBlank(apiParamModelNode.getName())) {
                    YapiTypeDTO yapiTypeDTO = nodeToBodyDTO(apiParamModelNode);
                    dtoMap.put(apiParamModelNode.getName(), yapiTypeDTO);
                }
            }
            yapiTypeDto.setProperties(dtoMap);
        }
        if (paramModelNode instanceof ApiRefPropNode) {
            String ref = ((ApiRefPropNode) paramModelNode).getRef();
            yapiTypeDto.setDescription(String.format("{引用属性:%s}:%s", ref, yapiTypeDto.getDescription()));
        }
        if (paramModelNode.getType() == ApiParamType.DATE) {
            yapiTypeDto.setDescription("{日期类型}" + yapiTypeDto.getDescription());
        }

        return yapiTypeDto;
    }

    /**
     * 转换成查询参数
     *
     * @param formParam
     * @return
     */
    private QueryItemDTO nodeToQueryDTO(BaseApiPropNode formParam) {
        QueryItemDTO queryItemDto = new QueryItemDTO();
        String desc = formParam.getDesc() == null ? "" : formParam.getDesc();
        if (formParam instanceof ApiEnumPropNode) {
            ApiEnumPropNode enumPropNode = (ApiEnumPropNode) formParam;
            desc = desc + enumPropNode.parseEnumDesc();
        }
        queryItemDto.setDesc(desc);
        setBaseInfo(formParam, queryItemDto);
        return queryItemDto;
    }

    private static void setBaseInfo(BaseApiPropNode formParam, QueryItemDTO queryItemDto) {
        queryItemDto.setExample(formParam.getExample());
        queryItemDto.setName(formParam.getName());
        queryItemDto.setRequired(formParam.isRequired() ? "1" : "0");
        queryItemDto.setExample(formParam.getExample());
    }


    // 接口调用

    private Integer getOrCreateCat(String catName, String catDesc) {
        //空分类
        if (StrUtil.isNotBlank(catName)) {
            //获取所有分类

            YapiResponse<List<CatDTO>> cats = this.getCats();
            Optional<CatDTO> catOptional = cats.getData()
                    .stream()
                    .filter(i -> StrUtil.equals(i.getName(), catName))
                    .findAny();

            if (catOptional.isPresent()) {
                CatDTO catDto = catOptional.get();
                if (StrUtil.isNotBlank(catDesc) && !catDesc.equals(catDto.getDesc())) {
                    //todo 更新desc


                }
                return catDto.getId();
            } else {
                //创建
                CatDTO cat = createCat(catName, catDesc);
                if (cat != null) {
                    return cat.getId();
                }
            }
        }
        return null;
    }

    public YapiResponse<List<CatDTO>> getCats() {
        Map<String, String> param = new HashMap<>();
        param.put("project_id", settings.getYapiProjectId());
        String resp = yapiClient.get(Yapis.GET_CAT_MENU, param);
        Type type = new TypeToken<YapiResponse<List<CatDTO>>>() {
        }.getType();
        YapiResponse<List<CatDTO>> response = JsonUtil.fromJson(resp, type);
        return response;
    }

    public CatDTO createCat(String name, String desc) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("desc", desc);
        String resp = yapiClient.post(Yapis.ADD_CAT, param);
        if (YapiResponse.isSuccess(resp)) {
            Type type = new TypeToken<YapiResponse<CatDTO>>() {
            }.getType();
            YapiResponse<CatDTO> yapiResponse = JsonUtil.fromJson(resp, type);
            return yapiResponse.getData();
        }
        return null;
    }


}
