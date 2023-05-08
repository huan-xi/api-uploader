package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.cons.PrimitiveType;
import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本数据类型
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiPrimitivePropNode extends BaseApiPropNode {
    //基础类型
    private final PrimitiveType primitiveType;

    public ApiPrimitivePropNode(PrimitiveType type) {
        Assert.notNull(type, "类型不能为空");
        this.primitiveType = type;
        this.setType(type.getParamType());
    }
}
