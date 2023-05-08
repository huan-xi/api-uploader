package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiListPropNode extends BaseApiPropNode {
    /**
     * 元素类型
     */
    private BaseApiPropNode elementType;
    public ApiListPropNode(){
        setType(ApiParamType.LIST);
    }
}
