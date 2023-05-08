package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;

/**
 * 对象类型
 */
public class ApiObjectPropNode extends BaseApiPropNode {
    public ApiObjectPropNode() {
        setType(ApiParamType.OBJECT);
    }
}
