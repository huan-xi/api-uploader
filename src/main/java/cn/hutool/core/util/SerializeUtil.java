package cn.hutool.core.util;

import cn.hperfect.apikit.utils.IoUtil;
import com.twelvemonkeys.io.FastByteArrayOutputStream;

import java.io.*;

/**
 * @author hperfect
 * @date 2023/5/7 10:40
 */
public class SerializeUtil {


    /**
     * 反序列化<br>
     * 对象必须实现Serializable接口
     *
     * <p>
     * 注意！！！ 此方法不会检查反序列化安全，可能存在反序列化漏洞风险！！！
     * </p>
     *
     * @param <T>   对象类型
     * @param bytes 反序列化的字节码
     * @return 反序列化后的对象
     */
    public static <T> T deserialize(byte[] bytes) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(ois);
            IoUtil.close(is);
        }

    }


    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            byte[] bytes = bos.toByteArray();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(out);
            IoUtil.close(bos);
        }
    }

}
