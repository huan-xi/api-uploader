package cn.hperfect.apikit.core.parser.base;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.cons.ApiKitTagCons;
import cn.hperfect.apikit.core.ApiModelParseContext;
import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.core.model.ApiModel;
import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.core.parser.ApiKitParserFactory;
import cn.hperfect.apikit.core.parser.IApiParamParser;
import cn.hperfect.apikit.core.parser.NameParser;
import cn.hperfect.apikit.enums.ApiType;
import cn.hperfect.apikit.enums.ControllerType;
import cn.hperfect.apikit.utils.ObjectUtil;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.*;

import java.util.*;

public abstract class BaseApiModelParser {
    /**
     * 一个方法可以解析出多个api
     *
     * @param apiCat
     * @param controllerType 控制器类型
     * @param method
     * @return
     */
    public List<ApiModel> parseApiModel(ApiCat apiCat, ControllerType controllerType, UMethod method) {

        ApiModelParseContext parseContext = new ApiModelParseContext();
        IApiParamParser apiParamPaster = ApiKitParserFactory.getApiParamPaster(parseContext, controllerType);
        List<ApiModel> apiModels = new ArrayList<>();
        //解析参数

        //解析返回值
        Map<ApiType, UAnnotation> types = new EnumMap<>(ApiType.class);
        for (ApiType value : ApiType.values()) {
            List<UAnnotation> uAnnotations = method.getUAnnotations();
            //找requestMap
            UAnnotation requestAnnotation = CollUtil.findOne(uAnnotations, i -> StrUtil.equals(i.getQualifiedName(), AnnotationCons.SPRING.REQUEST_MAPPING));
            if (requestAnnotation != null) {
                types.put(value, requestAnnotation);
            }
            //覆盖
            String name = StrUtil.upperFirst(value.name().toLowerCase(Locale.ROOT));
            String className = String.format("org.springframework.web.bind.annotation.%sMapping", name);
            UAnnotation annotation = CollUtil.findOne(uAnnotations, i -> StrUtil.equals(i.getQualifiedName(), className));
            if (annotation != null) {
                types.put(value, annotation);
            }
        }
        //解析types get,set,post
        for (ApiType apiType : types.keySet()) {
            UAnnotation annotation = types.get(apiType);
            List<String> paths = UAnnotationUtils.findListStr(annotation, "value", "path");
            ApiModel apiModel = new ApiModel();
            apiModel.setDeprecated(method.getAnnotation(AnnotationCons.Deprecated) != null);
            //名称
            apiModel.setName(parseApiName(method));
            //描述
            apiModel.setDesc(parseApiDesc(method));
            // api 类型

            apiModel.setApiType(apiType);
            List<UParameter> uastParameters = method.getUastParameters();
            //解析query参数
            apiModel.setQuery(apiParamPaster.parseQueryParam(method, uastParameters));
            //解析body
            apiModel.setBody(apiParamPaster.parseBodyParam(uastParameters));
            //返回值
            apiModel.setResultType(parseResultType(apiParamPaster, method));
            //多路径解析成多个api
            if (CollUtil.isEmpty(paths)) {
                paths.add("");
            }
            for (String catPath : apiCat.getPath()) {
                for (String path : paths) {
                    apiModels.add(ObjectUtil.cloneByStream(apiModel).setPath(catPath + path));
                }
            }
        }
        return apiModels;
    }

    /**
     * 解析返回值类型
     *
     * @return
     */
    protected BaseApiPropNode parseResultType(IApiParamParser apiParamPaster, UMethod method) {
        PsiType type = method.getReturnType();
        BaseApiPropNode apiObjectType = apiParamPaster.parseNodeByType(type);

        return apiObjectType;
    }


    /**
     * 从方法中解析api的描述信息
     * 1. notes 注解
     * 2. apiNote 注释tag中解析
     * 3. 注释
     *
     * @param method
     * @return
     */
    protected String parseApiDesc(@NotNull UMethod method) {
//        List<UComment> comments = method.getComments();
//        System.out.println("test");
        UAnnotation annotation = UAnnotationUtils.getAnnotation(method, AnnotationCons.SWAGGER.API_OPERATION);
        NameParser nameParser = new NameParser(
                annotation,
                method.getDocComment(),
                method.getName()
        );
        return nameParser.parseName("notes", ApiKitTagCons.METHOD.API_NOTE);
    }

    /**
     * 从方法中解析apiName名称
     * 1. swagger ApiOperation 注解
     * 2. apiName
     * 3. 方法注释
     *
     * @param method
     * @return
     */
    protected String parseApiName(UMethod method) {
        UAnnotation annotation = UAnnotationUtils.getAnnotation(method, AnnotationCons.SWAGGER.API_OPERATION);
        NameParser nameParser = new NameParser(
                annotation,
                method.getDocComment(), method.getName()
        );
        return nameParser.parseName("value", ApiKitTagCons.METHOD.API_NAME);
    }


    public abstract boolean checkIsApiMethod(UMethod method);
}
