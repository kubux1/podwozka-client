package podwozka.podwozka.Rest;

import org.apache.http.HttpHeaders;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ContentTypeInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        return chain.proceed(request);
    }
}
