package com.muhammed.citylabadmin.service;

import com.muhammed.citylabadmin.data.model.offer.AllOffer;
import com.muhammed.citylabadmin.data.model.login.LoginResponse;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;

import com.muhammed.citylabadmin.data.model.reservation.Booking;
import com.muhammed.citylabadmin.data.model.user.UsersResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    @FormUrlEncoded
    @POST("api/Users/Login")
    Single<LoginResponse> userLogin(@Field("PhoneNumber") String phone,
                                    @Field("deviceToken") String token);




    @FormUrlEncoded
    @POST("api/offers/upload")
    @Headers("Accept:application/json")
    Single<SimpleResponse> uploadOffer(@Field("Files")String image,
                                       @Field("title") String title,
                                       @Field("description") String desc,
                                       @Field("StartTime") String sDate,
                                       @Field("EndTime") String eDate,
                                       @Field("PreviousPrice") Double oldPrice,
                                       @Field("CurrentPrice") Double newPrice);


//    @Multipart
//    @POST("api/offers/upload")
//    @Headers("Accept:application/json")
//    Single<SimpleResponse> sendResult(@PartMap Map<String, List<MultipartBody.Part>> Files,
//                                      @Part("PhoneNumber") String phone);

    @FormUrlEncoded
    @POST("api/Results/Upload")
    @Headers("Accept:application/json")
    Single<SimpleResponse> sendResult(@Field("Files") List<String> files,
                                      @Field("PhoneNumber") String phone,
                                      @Field("notes") String note);


//    @POST("api/Results/Upload")
//    @Headers("Accept:application/json")
//    Single<SimpleResponse> sendResult(@Body ResultRequest request);


    @FormUrlEncoded
    @POST("api/Users/Register")
    @Headers("Accept:application/json")
    Single<SimpleResponse> addUser(@Field("Name") String name,
                                   @Field("PhoneNumber") String phone,
                                   @Field("deviceToken") String token);



    @DELETE("api/Reservations/{id}")
    @Headers("Accept:application/json")
    Call<SimpleResponse> delete(
            @Header("Authorization") String token,
            @Path("id") int id);

    @DELETE("api/offers/{id}")
    @Headers("Accept:application/json")
    Call<SimpleResponse> delete_offer(
            @Header("Authorization") String token,
            @Path("id") int id);

    @GET("api/Reservations/all")
    @Headers("Accept:application/json")
    Call<Booking> getAllReservation();

    @GET("api/offers/all")
    public Call<AllOffer>offers();

    @FormUrlEncoded
    @PUT("api/offers/{id}")
    @Headers("Accept:application/json")
      Call<SimpleResponse>updateOffer(
            @Field("Files")String image,
            @Field("title") String title,
            @Field("description") String desc,
            @Field("StartTime") String sDate,
            @Field("EndTime") String eDate,
            @Field("PreviousPrice") Double oldPrice,
            @Field("CurrentPrice") Double newPrice,
            @Header("Authorization") String token,
            @Path("id") int id

    );



    @GET("api/users/all")
    @Headers("Accept:application/json")
    Single<UsersResponse> getAllUsers();

}
