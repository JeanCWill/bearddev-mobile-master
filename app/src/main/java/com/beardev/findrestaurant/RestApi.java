package com.beardev.findrestaurant;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jean on 11/06/2016.
 */
public interface RestApi {

    @GET("gerenciaPosicoesBusao")
    Call<Localizacao> buscaUltimaLocalizacaoOnibus(@Query("codigoLinha") Integer codigoLinha );

    @POST("gerenciaPosicoesBusao")
    Call<Void> enviaLocalizacaoOnibus(@Body Onibus onibus);

    @GET("restaurants_mobile.json")
    Call<JsonArray> listRestaurants();

    @GET("photos_mobile.json")
    Call<JsonArray> listPhotos(@Query("restaurant_id") Integer restaurantId );

    @GET("menus_mobile.json")
    Call<JsonArray> listMenus(@Query("restaurant_id") Integer restaurantId );
/*
    @GET("linhas/{codigoLinha}")
    Call<Localizacao> buscaUltimaLocalizacaoOnibus(@Path("codigoLinha") Integer codigoLinha );

    @POST("pontos")
    Call<Integer> enviaLocalizacaoOnibus(@Query("lat") Double lat, @Query("lon") Double lon, @Query("linha_id") Integer id);*/
}
