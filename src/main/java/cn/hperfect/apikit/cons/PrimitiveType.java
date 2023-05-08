package cn.hperfect.apikit.cons;

import cn.hperfect.apikit.enums.ApiParamType;
import cn.hutool.core.collection.CollUtil;
import com.intellij.lang.jvm.types.JvmPrimitiveTypeKind;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * 基本数据类型
 */
public enum PrimitiveType {
    INT(ApiParamType.INT,"java.lang.Integer", "int"),
    STRING(ApiParamType.STRING,"java.lang.String"),
    DOUBLE(ApiParamType.DOUBLE,"java.lang.Double", "double"),
    LONG(ApiParamType.LONG,"java.lang.Long", "long"),
    FLOAT(ApiParamType.FLOAT,"java.lang.Float", "float"),
    BIG_DECIMAL(ApiParamType.BIG_DECIMAL,"java.math.BigDecimal"),

    DATE(ApiParamType.DATE,"java.sql.Timestamp",
            "java.util.Date",
            "java.time.LocalDate",
            "java.time.LocalTime",
            "java.time.LocalDateTime"),
    BOOLEAN(ApiParamType.BOOLEAN,"java.lang.Boolean", "boolean"),
    BYTE(ApiParamType.BYTE,"java.lang.Byte", "byte"),
    SHORT(ApiParamType.SHORT,"java.lang.Short", "short"),

    ;

    /**
     * 类名
     */
    @Getter
    private final List<String> classNames;
    @Getter
    private final ApiParamType paramType;


    PrimitiveType(ApiParamType paramType, String... classNames) {
        this.paramType = paramType;
        this.classNames = Arrays.asList(classNames);
    }

    /**
     * 匹配基本类型
     *
     * @param kind
     * @return
     */
    public static PrimitiveType matchType(@NotNull String kind) {
        for (PrimitiveType value : values()) {
            if (value.isMatch(kind)) {
                return value;
            }
        }
        return null;
    }

    /**
     * class -> 类型装换
     *
     * @param name
     * @return
     */
    public boolean isMatch(String name) {
        return CollUtil.contains(classNames, name);
    }

}
