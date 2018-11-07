package podwozka.podwozka.Rest;

import org.apache.http.HttpHeaders;

import java.util.List;

import podwozka.podwozka.entity.PassengerTravelDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface PassengerTravelService {
    @GET("api/passenger/travels")
    Call<List<PassengerTravelDTO>> getAllUserTravels(@Query("login") String login,
                                            @Query("page") int pageIndex,
                                            @Header(HttpHeaders.AUTHORIZATION) String authHeader);
}
