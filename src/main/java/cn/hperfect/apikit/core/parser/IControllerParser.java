package cn.hperfect.apikit.core.parser;

import cn.hperfect.apikit.core.model.ApiCat;
import cn.hperfect.apikit.core.model.ApiModel;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * 控制器解析器->cat
 */
public interface IControllerParser {
    /**
     * 解析分类
     *
     * @param psiClass
     * @param selectedMethod
     * @return
     */
    ApiCat parse(PsiClass psiClass, PsiMethod selectedMethod);

    /**
     * 解析方法
     *
     * @param psiMethod
     * @return
     */
    ApiModel parse(PsiMethod psiMethod);

}
