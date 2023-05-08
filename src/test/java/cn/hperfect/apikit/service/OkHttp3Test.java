package cn.hperfect.apikit.service;

import cn.hperfect.apikit.cons.Yapis;
import cn.hperfect.apikit.core.http.HttpClientFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author hperfect
 * @date 2023/5/7 15:38
 */
public class OkHttp3Test {
    public static void main(String[] args) throws IOException {

       /* OkHttpClient httpClient = HttpClientFactory.ME.getHttpClient();
        new Request.Builder().url("https://www.baidu.com")
                .post()
                .build();*/
     /*HttpResponse response = HttpUtil.createPost(settings.getYapiHost() + Yapis.ADD_CAT)
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .form("project_id", settings.getYapiProjectId())
                .form("token", settings.getYapiToken())
                .form("name", name)
                .form("desc", desc)
                .execute();*/

       /* OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        OkHttpClient httpClient = clientBuilder.build();
        Request request = new Request.Builder().url("https://www.baidu.com").get().build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response.body().string());;*/

    }
}
