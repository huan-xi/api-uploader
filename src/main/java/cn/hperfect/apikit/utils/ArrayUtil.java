package cn.hperfect.apikit.utils;

import cn.hutool.core.lang.Assert;
import org.hamcrest.Matcher;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author hperfect
 * @date 2023/5/7 10:57
 */
public class ArrayUtil {
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 返回数组中第一个匹配规则的值
     *
     * @param <T>     数组元素类型
     * @param matcher 匹配接口，实现此接口自定义匹配规则
     * @param array   数组
     * @return 匹配元素，如果不存在匹配元素或数组为空，返回 {@code null}
     * @since 3.0.7
     */
    @SuppressWarnings("unchecked")
    public static <T> T firstMatch(Predicate<T> matcher, T... array) {
        final int index = matchIndex(matcher, array);
        if (index < 0) {
            return null;
        }

        return array[index];
    }

    /**
     * 返回数组中第一个匹配规则的值的位置
     *
     * @param <T>     数组元素类型
     * @param matcher 匹配接口，实现此接口自定义匹配规则
     * @param array   数组
     * @return 匹配到元素的位置，-1表示未匹配到
     * @since 5.6.6
     */
    @SuppressWarnings("unchecked")
    public static <T> int matchIndex(Predicate<T> matcher, T... array) {
        return matchIndex(matcher, 0, array);
    }

    /**
     * 返回数组中第一个匹配规则的值的位置
     *
     * @param <T>               数组元素类型
     * @param matcher           匹配接口，实现此接口自定义匹配规则
     * @param beginIndexInclude 检索开始的位置
     * @param array             数组
     * @return 匹配到元素的位置，-1表示未匹配到
     * @since 5.7.3
     */
    @SuppressWarnings("unchecked")
    public static <T> int matchIndex(Predicate<T> matcher, int beginIndexInclude, T... array) {
        Assert.notNull(matcher, "Matcher must be not null !");
        if (isNotEmpty(array)) {
            for (int i = beginIndexInclude; i < array.length; i++) {
                if (matcher.test(array[i])) {
                    return i;
                }
            }
        }

        return INDEX_NOT_FOUND;
    }


    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return (null != array && array.length != 0);
    }

}
