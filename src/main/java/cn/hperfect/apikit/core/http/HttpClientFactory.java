package cn.hperfect.apikit.core.http;

import lombok.Getter;
import okhttp3.OkHttpClient;

/**
 * @author hperfect
 * @date 2023/5/7 17:25
 */
public enum HttpClientFactory {
    ME,
    ;
    @Getter
    private final OkHttpClient httpClient;

    HttpClientFactory() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        this.httpClient = clientBuilder.build();

    }

}
