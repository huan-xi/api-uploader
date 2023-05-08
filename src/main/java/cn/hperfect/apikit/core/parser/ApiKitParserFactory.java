package cn.hperfect.apikit.core.parser;

import cn.hperfect.apikit.core.ApiModelParseContext;
import cn.hperfect.apikit.core.parser.base.BaseApiCatParser;
import cn.hperfect.apikit.core.parser.base.BaseApiModelParser;
import cn.hperfect.apikit.core.parser.spring.SpringApiCatParserImpl;
import cn.hperfect.apikit.core.parser.impl.ControllerParserImpl;
import cn.hperfect.apikit.core.parser.spring.SpringApiModelParserImpl;
import cn.hperfect.apikit.core.parser.spring.SpringApiParamParserImpl;
import cn.hperfect.apikit.enums.ControllerType;
import com.google.common.cache.Cache;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1. class 解析分类
 * <p>
 * api kit 解析器
 */
public class ApiKitParserFactory {

    private static Map<String, PsiClass> CLASS_MAP = new HashMap<>();

    private static Project project = null;

//    private static PsiClass mapClass = null;
//    private static PsiClass iterableClass = null;

    @Nullable
    public static synchronized PsiClass getPsiClassByName(String className) {
        if (CLASS_MAP.containsKey(className)) {
            return CLASS_MAP.get(className);
        }
        PsiClass psiClass = JavaPsiFacade.getInstance(project)
                .findClass(className,
                        GlobalSearchScope.allScope(project));
        CLASS_MAP.put(className, psiClass);
        return psiClass;
    }


    public static PsiClass getMapPsiClass() {
        return getPsiClassByName(Map.class.getName());
    }

    public static PsiClass getIterablePsiClass() {
        return getPsiClassByName(Iterable.class.getName());
    }

    public static IControllerParser getControllerParser() {
        return ControllerParserImpl.ME;
    }

    public static BaseApiCatParser getApiCatParser(ControllerType controllerType) {
        return SpringApiCatParserImpl.instance;
    }

    public static IApiParamParser getApiParamPaster(ApiModelParseContext parseContext, ControllerType controllerType) {
        return new SpringApiParamParserImpl(parseContext);
    }


    public static BaseApiModelParser getApiModelParser(ControllerType controllerType) {
        return SpringApiModelParserImpl.instance;
    }


    public static void setProject(Project project) {
        ApiKitParserFactory.project = project;
    }
}
