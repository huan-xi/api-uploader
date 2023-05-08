package cn.hperfect.apikit.core.parser.spring;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.core.parser.base.BaseApiModelParser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UMethod;

import java.util.List;
import java.util.stream.Collectors;

public class SpringApiModelParserImpl extends BaseApiModelParser {
    public static SpringApiModelParserImpl instance = new SpringApiModelParserImpl();
    private SpringApiModelParserImpl() {

    }

    final List<String> REQUEST_ANNOTATION = ListUtil.toList(
            AnnotationCons.SPRING.GET_MAPPING,
            AnnotationCons.SPRING.REQUEST_MAPPING,
            AnnotationCons.SPRING.DELETE_MAPPING,
            AnnotationCons.SPRING.PUT_MAPPING,
            AnnotationCons.SPRING.POST_MAPPING
    );


    @Override
    public boolean checkIsApiMethod(UMethod method) {

        List<String> annotationsName = method.getUAnnotations()
                .stream().map(UAnnotation::getQualifiedName)
                .collect(Collectors.toList());

        return CollUtil.containsAny(annotationsName, REQUEST_ANNOTATION);
    }
}
