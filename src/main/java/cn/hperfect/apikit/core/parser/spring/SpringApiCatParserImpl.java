package cn.hperfect.apikit.core.parser.spring;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.core.parser.base.BaseApiCatParser;
import cn.hperfect.apikit.utils.UAnnotationUtils;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.impl.compiled.ClsArrayInitializerMemberValueImpl;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpringApiCatParserImpl extends BaseApiCatParser {
    public static SpringApiCatParserImpl instance = new SpringApiCatParserImpl();
    private SpringApiCatParserImpl() {

    }

    @Override

    protected List<String> parsePath(UClass controllerClass) {
        UAnnotation annotation = UAnnotationUtils.getAnnotation(controllerClass, AnnotationCons.SPRING.REQUEST_MAPPING);
        Assert.notNull(annotation, "@RequestMapping不存在");
        return UAnnotationUtils.findListStr(annotation, "path", "value");
    }


}
