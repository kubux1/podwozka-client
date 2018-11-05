package podwozka.podwozka.Rest;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import podwozka.podwozka.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class APIClient {

    private static final String scheme = "http";
    private static final String hostIP = "192.168.43.152";
    private static final int port = 8080;
    private static final HttpUrl hostUrl = new HttpUrl.Builder()
            .scheme(scheme)
            .host(hostIP)
            .port(port)
            .build();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new ContentTypeInterceptor())
            .build();

    private static Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(Constants.getDefaultObjectMapper()))
            .client(client)
            .baseUrl(hostUrl)
            .build();

    public static TravelService getTravelService() {
        return retrofit.create(TravelService.class);
    }
}
