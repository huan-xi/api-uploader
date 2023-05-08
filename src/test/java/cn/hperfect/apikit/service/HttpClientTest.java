package cn.hperfect.apikit.service;

import cn.hperfect.apikit.utils.IoUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author hperfect
 * @date 2023/5/7 14:11
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {

        //1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
//2.生成一个get请求
        HttpGet httpget = new HttpGet("https://www.baidu.com/");
//3.执行get请求并返回结果
        CloseableHttpResponse response = httpclient.execute(httpget);
        System.out.println("test");
//        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("13");

        try {
            //4.处理结果
        } finally {
            IoUtil.close(response);
        }
    }
}
