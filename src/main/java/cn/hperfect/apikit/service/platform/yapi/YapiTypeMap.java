package cn.hperfect.apikit.service.platform.yapi;

import cn.hperfect.apikit.enums.ApiParamType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hperfect
 * @date 2023/5/7 18:19
 */
public class YapiTypeMap {
    public static final Map<ApiParamType, String> MAP = new HashMap<>();

    static {
        MAP.put(ApiParamType.MAP, "object");
        MAP.put(ApiParamType.OBJECT, "object");
        MAP.put(ApiParamType.LIST, "array");
        MAP.put(ApiParamType.BOOLEAN, "boolean");
        MAP.put(ApiParamType.STRING, "string");
        MAP.put(ApiParamType.ENUM, "string");
        MAP.put(ApiParamType.LONG, "number");
        MAP.put(ApiParamType.INT, "number");
        MAP.put(ApiParamType.DOUBLE, "number");
        MAP.put(ApiParamType.BIG_DECIMAL, "number");
        MAP.put(ApiParamType.FLOAT, "number");
        MAP.put(ApiParamType.SHORT, "number");
    }
}
