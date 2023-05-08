package cn.hperfect.apikit.core.parser.spring;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.cons.IgnoreConfigCons;
import cn.hperfect.apikit.core.ApiModelParseContext;
import cn.hperfect.apikit.core.parser.ApiKitParserFactory;
import cn.hperfect.apikit.core.parser.base.BaseApiParamParser;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UParameter;

import java.util.List;

public class SpringApiParamParserImpl extends BaseApiParamParser {

    final ApiModelParseContext context;

    public SpringApiParamParserImpl(ApiModelParseContext context) {
        this.context = context;
    }

    @Override
    protected boolean queryParamIsFiltered(UParameter parameter) {
        // requestBody为body 参数,忽略
        PsiAnnotation requestBody = parameter.getAnnotation(AnnotationCons.SPRING.REQUEST_BODY);
        if (requestBody != null) {
            return true;
        }
        List<UAnnotation> uAnnotations = parameter.getUAnnotations();
        if (CollUtil.isNotEmpty(uAnnotations)) {
            //存在注解，但不是swagger 和spring 参数描述,多半是自定义注解，直接忽略掉
            UAnnotation one = CollUtil.findOne(uAnnotations, i -> StrUtil.equals(i.getQualifiedName(), AnnotationCons.SPRING.REQUEST_PARAM)
                    || StrUtil.equals(i.getQualifiedName(), AnnotationCons.SWAGGER.API_PARAM)
                    || StrUtil.equals(i.getQualifiedName(), AnnotationCons.Validated)
            );
            if (one == null) {
                return true;
            }
        }
        //忽略注入属性
        PsiType type = parameter.getType();
        if (type instanceof PsiClassReferenceType) {
            String className = ((PsiClassReferenceType) type).getClassName();
            String one = CollUtil.findOne(IgnoreConfigCons.QUERY_PARAM_PACKAGES, p -> StrUtil.contains(className, p));
            //被包忽略掉了
            if (one != null) {
                return true;
            }
        }


        return false;
    }

    /**
     * 1. 从 @RequestParam 中获取
     * 2. 默认就是参数名
     * <p>
     * 解析参数名称
     *
     * @param parameter
     * @param annotation
     * @param annoApiParam
     * @return
     */
    @Override
    protected String parseApiParamName(UParameter parameter, UAnnotation annotation, UAnnotation annoApiParam) {
        //解析参数名称
        if (annotation != null) {
            String str = UAnnotationUtils.findStr(annotation, "value", "name");
            if (StrUtil.isNotBlank(str)) {
                return str;
            }
        }
        //从swagger 参数中获取
        if (annoApiParam != null) {
            String name = UAnnotationUtils.findStr(annoApiParam, "name");
            if (StrUtil.isNotBlank(name)) {
                return name;
            }
        }
        // 从参数中获取
        return parameter.getName();
    }

    @Override
    protected boolean psiClassIsFileType(PsiClass psiClass) {
        PsiClass fileClass = ApiKitParserFactory.getPsiClassByName("org.springframework.web.multipart.MultipartFile");
        if (fileClass == null) {
            return false;
        }
        return psiClass.isInheritor(fileClass, true);
    }
}
