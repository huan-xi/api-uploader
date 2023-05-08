package cn.hperfect.apikit.core.parser.base;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.utils.PsiAnnotationUtils;
import cn.hperfect.apikit.utils.PsiDocCommentUtils;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

import java.util.List;

/**
 * 分类解析器
 */
public abstract class BaseApiCatParser {


    public ApiCat parse(UClass controllerClass) {
        ApiCat apiCat = new ApiCat();
        //名称解析
        apiCat.setCatName(parseName(controllerClass));
        apiCat.setCatDesc(PsiDocCommentUtils.parseDesc(controllerClass.getDocComment()));
        apiCat.setPath(parsePath(controllerClass));
        return apiCat;
    }

    /**
     * 解析路径
     *
     * @param controllerClass
     * @return
     */
    protected abstract List<String> parsePath(UClass controllerClass);


    /**
     * 解析分类名称
     *
     * @return
     */
    protected String parseName(UClass uClass) {
        UAnnotation annotation = UAnnotationUtils.getAnnotation(uClass.getUAnnotations(), AnnotationCons.SWAGGER.API);
        String name = null;
        if (annotation != null) {
            List<String> tags = UAnnotationUtils.findListStr(annotation, "tags");
            if (CollUtil.isNotEmpty(tags)) {
                name = tags.get(0);
            }
        }
        //将描述作为名称
        if (StrUtil.isBlank(name)) {
            name = PsiDocCommentUtils.parseDesc(uClass.getDocComment());
        }
        //class name
        if (StrUtil.isBlank(name)) {
            name = uClass.getName();
        }
        return name;
    }
}
