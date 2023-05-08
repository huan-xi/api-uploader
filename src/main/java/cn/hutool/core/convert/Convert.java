package cn.hutool.core.convert;

/**
 * @author hperfect
 * @date 2023/5/7 10:38
 */
public class Convert {

    public static String toStr(Object value) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }
}
