package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * map 类型 map 的两个泛型，需要？
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiMapPropNode extends BaseApiPropNode {
    public ApiMapPropNode() {
        setType(ApiParamType.MAP);
    }
}
