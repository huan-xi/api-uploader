package cn.hperfect.apikit.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.jetbrains.uast.UAnnotated;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.java.JavaAnnotationArrayInitializerUCallExpression;
import org.jetbrains.uast.java.JavaULiteralExpression;
import org.jetbrains.uast.java.UnknownJavaExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UAnnotationUtils {
    /**
     * 查找注解中的 string数组
     *
     * @param annotation
     * @param attrs
     * @return
     */
    public static List<String> findListStr(UAnnotation annotation, String... attrs) {
        for (String attr : attrs) {
            UExpression value = annotation.findAttributeValue(attr);
            if (value instanceof JavaAnnotationArrayInitializerUCallExpression) {
                JavaAnnotationArrayInitializerUCallExpression expression = (JavaAnnotationArrayInitializerUCallExpression) value;
                List<UExpression> valueArguments = expression.getValueArguments();
                if (CollUtil.isNotEmpty(valueArguments)) {
                    return valueArguments.stream().map(UAnnotationUtils::getStringValue).collect(Collectors.toList());
                }
            } else if (value instanceof JavaULiteralExpression) {
                return ListUtil.toList(getStringValue(value));
            }

        }
        return new ArrayList<>();
    }

    private static String getStringValue(UExpression expression) {
        if (expression instanceof UnknownJavaExpression) {
            return "";
        }
        Assert.isTrue(expression instanceof JavaULiteralExpression, "该value 不是string 类型");
        assert expression instanceof JavaULiteralExpression;
        JavaULiteralExpression value = (JavaULiteralExpression) expression;
        return value.evaluateToString();
    }

    public static UAnnotation getAnnotation(UAnnotated uAnnotated, String annotateName) {
        return getAnnotation(uAnnotated.getUAnnotations(), annotateName);
    }

    public static UAnnotation getAnnotation(List<UAnnotation> uAnnotations, String annotateName) {
        return CollUtil.findOne(uAnnotations, i -> StrUtil.equals(i.getQualifiedName(), annotateName));
    }

    /**
     * 获取注解 字符串
     *
     * @param annotation
     * @param attrs
     * @return
     */
    public static String findStr(UAnnotation annotation, String... attrs) {
        for (String attr : attrs) {
            UExpression value = annotation.findAttributeValue(attr);
            String stringValue = getStringValue(value);
            if (StrUtil.isNotBlank(stringValue)) {
                return stringValue;
            }
        }
        return "";
    }

}
