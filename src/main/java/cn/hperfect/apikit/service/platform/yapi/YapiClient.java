package cn.hperfect.apikit.service.platform.yapi;

import cn.hperfect.apikit.core.http.HttpClientFactory;
import cn.hperfect.apikit.settings.AppSettingsState;
import cn.hperfect.apikit.utils.IoUtil;
import cn.hperfect.apikit.utils.JsonUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author hperfect
 * @date 2023/5/7 18:33
 */
public class YapiClient {

    private final AppSettingsState settings;

    public YapiClient(AppSettingsState settings) {
        this.settings = settings;
    }

    public String post(String api, Map<String, String> param) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", settings.getYapiToken());
        builder.add("project_id", settings.getYapiProjectId());
        param.forEach(builder::add);
        Request request = new Request.Builder()
                .url(settings.getYapiHost() + api)
                .post(builder.build())
                .build();
        Response resp = null;
        try {
            resp = HttpClientFactory.ME.getHttpClient().newCall(request).execute();
            return resp.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(resp);
        }
    }

    public String get(String api, Map<String, String> param) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(settings.getYapiHost() + api).newBuilder();
        param.forEach(urlBuilder::addQueryParameter);
        urlBuilder.addQueryParameter("token", settings.getYapiToken());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Response resp = null;
        try {
            resp = HttpClientFactory.ME.getHttpClient().newCall(request).execute();
            String string = resp.body().string();
            if (resp.isSuccessful()) {
                return string;
            } else {
                throw new RuntimeException(String.format("调用接口:%s错误,原因%s", api, string));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(resp);
        }
    }

    public String postJson(String api, Object obj) {
        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.toJson(obj));
        Request request = new Request.Builder()
                .url(settings.getYapiHost() + api)
                .post(requestBodyJson)
                .build();

        Response resp = null;
        try {
            resp = HttpClientFactory.ME.getHttpClient().newCall(request).execute();
            return resp.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(resp);
        }
    }
}
