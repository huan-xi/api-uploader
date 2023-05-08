package cn.hperfect.apikit.utils;

import cn.hperfect.apikit.core.model.ApiModel;
import com.android.aapt.Resources;
import org.apache.http.client.utils.CloneUtils;

import javax.annotation.Nullable;

/**
 * @author hperfect
 * @date 2023/5/7 11:08
 */
public class ObjectUtil {

    public static <T> T cloneByStream(T obj) {
        try {
            return CloneUtils.cloneObject(obj);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
