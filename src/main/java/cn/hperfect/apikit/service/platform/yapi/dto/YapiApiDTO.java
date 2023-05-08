package cn.hperfect.apikit.service.platform.yapi.dto;

import cn.hutool.core.util.StrUtil;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author huanxi
 * @version 1.0
 * @date 2021/2/3 3:16 下午
 */
@Data
@Accessors(chain = true)
public class YapiApiDTO {
    /**
     * title
     */
    private String title;
    @SerializedName("catid")
    private Integer catId;
    private String method;
    private String token;
    private String path;
    private String desc;
    private String status = "done";

    @SerializedName("req_query")
    private List<QueryItemDTO> reqQuery;
    /**
     * 请求 类型 raw,form,json
     */
    @SerializedName("req_body_type")
    private String reqBodyType = "json";
    @SerializedName("req_body_other")
    private String reqBodyOther;

    /**
     * 返回参数类型  json
     */
    @SerializedName("res_body_type")
    private String resBodyType = "json";

    /**
     * 返回参数
     */
    @SerializedName("res_body")
    private String resBody;
    @SerializedName("req_body_is_json_schema")
    private boolean reqBodyIsJsonSchema = true;

    /**
     * 返回参数是否为json_schema
     */
    @SerializedName("res_body_is_json_schema")
    private boolean resBodyIsJsonSchema = true;

    @SerializedName("switch_notice")
    private boolean switchNotice = false;



    public YapiApiDTO setPath(String path) {
        if (StrUtil.isNotBlank(path) && path.charAt(0) != '/') {
            path = "/" + path;
        }
        this.path = path;
        return this;
    }
}
