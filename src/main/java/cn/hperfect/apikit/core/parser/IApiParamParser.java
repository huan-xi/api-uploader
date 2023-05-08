package cn.hperfect.apikit.core.parser;

import cn.hperfect.apikit.core.model.BaseApiPropNode;
import com.intellij.psi.PsiType;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UParameter;

import java.util.List;

/**
 * @author hperfect
 * @date 2023/5/7 14:33
 */
public interface IApiParamParser {
    /**
     * 解析body参数
     *
     * @param parameterList
     * @return
     */
    BaseApiPropNode parseBodyParam(List<UParameter> parameterList);

    /**
     * 解析query 参数
     *
     * @param method        需要用到方法中的doc
     * @param parameterList
     * @return
     */
    List<BaseApiPropNode> parseQueryParam(UMethod method, List<UParameter> parameterList);

    /**
     * 解析类型成属性节点
     *
     * @param type
     * @return
     */
    BaseApiPropNode parseNodeByType(PsiType type);
}
