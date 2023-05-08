package cn.hperfect.apikit.core.parser.impl;

import cn.hperfect.apikit.cons.AnnotationCons;
import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.core.model.ApiModel;
import cn.hperfect.apikit.core.parser.ApiKitParserFactory;
import cn.hperfect.apikit.core.parser.base.BaseApiCatParser;
import cn.hperfect.apikit.core.parser.base.BaseApiModelParser;
import cn.hperfect.apikit.core.parser.IControllerParser;
import cn.hperfect.apikit.enums.ControllerType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.intellij.psi.*;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UastContextKt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum ControllerParserImpl implements IControllerParser {
    ME;


    @Override
    public ApiCat parse(PsiClass psiClass, PsiMethod selectedMethod) {
        UClass uClass = UastContextKt.toUElement(psiClass, UClass.class);
        assert uClass != null : "选择区域没有找到控制器";
        ControllerType controllerType = parseControllerType(uClass);
        if (controllerType == null) {
            //不是控制器
            return null;
        }
        BaseApiCatParser apiCatParser = ApiKitParserFactory.getApiCatParser(controllerType);
        BaseApiModelParser apiModelParser = ApiKitParserFactory.getApiModelParser(controllerType);

        //解析api信息
        ApiCat apiCat = apiCatParser.parse(uClass);
        //将方法解析成api模型
        List<ApiModel> apiModels = new ArrayList<>();
        apiCat.setApiModels(apiModels);
        for (UMethod method : uClass.getMethods()) {
            //是否是当前光标所在的的api方法,todo 参数判断
            if (selectedMethod != null && !StrUtil.equals(selectedMethod.getName(), method.getName())) {
                continue;
            }
            //判断是否是api method
            if (!apiModelParser.checkIsApiMethod(method)) {
                continue;
            }
            List<ApiModel> parseResult = apiModelParser.parseApiModel(apiCat,controllerType, method);
            //解析api模型
            if (CollUtil.isNotEmpty(parseResult)) {
                apiModels.addAll(parseResult);
            }
            if (selectedMethod != null) {
                break;
            }
        }
        return apiCat;
    }

    /**
     * 判断是否是Controller
     *
     * @param uClass
     * @return
     */
    private ControllerType parseControllerType(UClass uClass) {
        List<String> annotationsName =
                uClass.getUAnnotations()
                        .stream()
                        .map(UAnnotation::getQualifiedName)
                        .collect(Collectors.toList());
        if (CollUtil.containsAny(annotationsName, ListUtil.toList(AnnotationCons.SPRING.REST_CONTROLLER, AnnotationCons.SPRING.CONTROLLER))) {
            return ControllerType.SPRING;
        }
        return null;
    }


    @Override
    public ApiModel parse(PsiMethod psiMethod) {
        return null;
    }
}
