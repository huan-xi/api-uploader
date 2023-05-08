package cn.hperfect.apikit.core.model.type;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import cn.hperfect.apikit.enums.ApiParamType;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.PsiField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 枚举类型
 * 枚举值
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiEnumPropNode extends BaseApiPropNode {
    /**
     * 枚举值
     */
    @Data
    @AllArgsConstructor
    public static class EnumValue implements Serializable {

        /**
         * 名称大写
         */
        private String name;
        /**
         * 描述
         */
        private String desc;
    }

    /**
     * 枚举值列表
     */
    private List<EnumValue> values;

    public ApiEnumPropNode(List<EnumValue> values) {
        this.values = values;
        setType(ApiParamType.ENUM);
    }

    public String parseEnumDesc() {
        StringBuilder remarkBuilder = new StringBuilder();
        for (EnumValue value : values) {
            String comment = value.getDesc();
            comment = "" + (StrUtil.isBlank(comment) ? "暂无注释" : comment);
            remarkBuilder.append(value.name)
                    .append(':')
                    .append(comment);
            remarkBuilder.append(";");
        }
        return String.format("{枚举值:%s}", remarkBuilder);
    }
}
