package podwozka.podwozka.Rest;

import org.apache.http.HttpHeaders;

import java.util.List;

import podwozka.podwozka.entity.TravelDTO;
import podwozka.podwozka.entity.TravelUser;
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
                                                @Header(HttpHeaders.AUTHORIZATION)
                                                        String authHeader);

    @GET("api/travels/coming")
    Call<List<TravelDTO>> getAllUserComingTravels(@Query("login") String login,
                                                  @Query("page") int pageIndex,
                                                  @Header(HttpHeaders.AUTHORIZATION)
                                                          String authHeader);

    @POST("api/travels/findMatching")
    Call<List<TravelDTO>> finMatching(@Query("page") int pageIndex,
                                      @Body TravelDTO travelDTO,
                                      @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @POST("api/travels")
    Call<TravelDTO> createTravel(@Body TravelDTO travelDTO,
                                 @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @POST("api/travels/signUp")
    Call<Void> signUpForTravel(@Query("login") String login,
                                    @Query("travelId") Long travelId,
                                    @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @PUT("api/travels/signedUpPassenger")
    Call<TravelUser> acceptTravel(@Body TravelUser travelDTO,
                                 @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @GET("api/travels/signedUpPassenger")
    Call<List<TravelUser>> getSignedUpPassenger(@Query("login") String login,
                                          @Query("travelId") Long travelId,
                              @Header(HttpHeaders.AUTHORIZATION) String authHeader);


    @PUT("api/travels")
    Call<TravelDTO> updateTravel(@Body TravelDTO travelDTO,
                                 @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @GET("api/travels/{id}")
    Call<TravelDTO> getTravel(@Path("id") Long id,
                              @Header(HttpHeaders.AUTHORIZATION) String authHeader);

    @DELETE("api/travels/delete/{id}")
    Call<Void> deleteTravel(@Path("id") Long id,
                            @Header(HttpHeaders.AUTHORIZATION) String authHeader);
}
