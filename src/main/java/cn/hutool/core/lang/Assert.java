package cn.hutool.core.lang;

import cn.hutool.core.util.StrUtil;
import org.hamcrest.Matcher;

/**
 * @author hperfect
 * @date 2023/5/7 10:27
 */
public class Assert {

    public static <T> void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isTrue(boolean b, String msg) {
        if (!b) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notBlank(String str, String msg) {
        if (StrUtil.isBlank(str)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
