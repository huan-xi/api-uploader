package cn.hperfect.apikit.core.parser;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.cons.ApiKitTagCons;
import cn.hperfect.apikit.utils.PsiAnnotationUtils;
import cn.hperfect.apikit.utils.PsiDocCommentUtils;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.uast.UAnnotation;

/**
 * api名称解析器
 */
public class NameParser {

    private final PsiDocComment doc;
    private final UAnnotation annotation;
    private final String finalVal;

    public NameParser(UAnnotation psiAnnotation, PsiDocComment doc, String finalVal) {
        this.doc = doc;
        this.annotation = psiAnnotation;
        this.finalVal = finalVal;
    }

    public String parseName(String annotationAttr, String tagName) {
        String name = null;
        if (annotation != null) {
            name = UAnnotationUtils.findStr(annotation, annotationAttr);
        }
        if (StrUtil.isBlank(name) && doc != null) {
            //@apiName 解析
            name = PsiDocCommentUtils.getJavaDocTagValue(doc, tagName);
            //注释解析
            if (StrUtil.isBlank(name)) {
                name = PsiDocCommentUtils.parseDesc(doc);
            }
        }
        if (StrUtil.isBlank(name)) {
            //方法方法名称
            name = finalVal;
        }
        return name;
    }
}
