package cn.hperfect.apikit.cons;

import cn.hutool.core.collection.ListUtil;

import java.util.List;

/**
 * @author hperfect
 * @date 2023/5/7 14:45
 */
public interface IgnoreConfigCons {
    /**
     * 忽略query参数解析的包 或类名
     */
    List<String> QUERY_PARAM_PACKAGES = ListUtil.toList("javax.servlet.http");

}
