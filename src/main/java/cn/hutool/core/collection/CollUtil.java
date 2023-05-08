package cn.hutool.core.collection;

import org.jetbrains.uast.UAnnotation;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author hperfect
 * @date 2023/5/7 10:12
 */
public class CollUtil {
    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return false == isEmpty(collection);
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，即是两个集合是否至少有一个共同的元素
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return 其中一个集合在另一个集合中是否至少包含一个元素
     * @see #intersection
     * @since 2.1
     */
    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 查找第一个匹配元素对象
     *
     * @param <T>        集合元素类型
     * @param collection 集合
     * @param filter     过滤器，满足过滤条件的第一个元素将被返回
     * @return 满足过滤条件的第一个元素
     * @since 3.1.0
     */
    public static <T> T findOne(Iterable<T> collection, Predicate<T> filter) {
        if (null != collection) {
            for (T t : collection) {
                if (filter.test(t)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 判断指定集合是否包含指定值，如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     *
     * @param collection 集合
     * @param value      需要查找的值
     * @return 如果集合为空（null或者空），返回{@code false}，否则找到元素返回{@code true}
     * @throws ClassCastException   如果类型不一致会抛出转换异常
     * @throws NullPointerException 当指定的元素 值为 null ,或集合类不支持null 时抛出该异常
     * @see Collection#contains(Object)
     * @since 4.1.10
     */
    public static boolean contains(Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }
}
