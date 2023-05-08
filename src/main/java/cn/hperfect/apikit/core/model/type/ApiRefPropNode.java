package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 引用节点
 * @author hperfect
 * @date 2023/5/8 11:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiRefPropNode extends ApiObjectPropNode {
    private final String ref;
}
