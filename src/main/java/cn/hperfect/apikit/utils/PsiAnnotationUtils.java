package cn.hperfect.apikit.utils;

import cn.hutool.core.convert.Convert;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiLiteralExpression;

public class PsiAnnotationUtils {
    public static Boolean getBooleanVal(PsiAnnotation annotation, String name) {
        PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue(name);
        if (attributeValue != null) {
            PsiLiteralExpression expression = (PsiLiteralExpression) attributeValue;
            Object value = expression.getValue();
            return (Boolean) value;
        }
        return null;
    }

    /**
     * 通过属性名称获取注解 string 值
     *
     * @param annotation
     * @param name
     * @return
     */
    public static String getStrValueByName(PsiAnnotation annotation, String name) {
        PsiAnnotationMemberValue attributeValue = annotation.findAttributeValue(name);
        if (attributeValue != null) {
            PsiLiteralExpression expression = (PsiLiteralExpression) attributeValue;
            return Convert.toStr(expression.getValue());
        }

        return null;
    }
}
