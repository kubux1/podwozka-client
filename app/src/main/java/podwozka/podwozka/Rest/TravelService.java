package podwozka.podwozka.Rest;

import org.apache.http.HttpHeaders;

import java.util.List;

import podwozka.podwozka.entity.TravelDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TravelService {
    @GET("api/travels")
    Call<List<TravelDTO>> getAllUserTravels(@Query("login") String login,
                                            @Query("page") int pageIndex,
                                            @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @GET("api/travels/past")
    Call<List<TravelDTO>> getAllUserPastTravels(@Query("login") String login,
                                            @Query("page") int pageIndex,
                                            @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @GET("api/travels/coming")
    Call<List<TravelDTO>> getAllUserComingTravels(@Query("login") String login,
                                            @Query("page") int pageIndex,
                                            @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @POST("api/travels")
    Call<List<TravelDTO>> createTravel(@Body TravelDTO travelDTO,
                                       @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @PUT("api/travels")
    Call<List<TravelDTO>> updateTravel(@Body TravelDTO travelDTO,
                                       @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @GET("api/travels/{id}")
    Call<List<TravelDTO>> getTravel(@Path("id") Long id,
                                    @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @DELETE("api/travels/delete/{id}")
    Call<Void> deleteTravel(@Path("id") Long id,
                                       @Header(HttpHeaders.AUTHORIZATION) String authHeader);
}
