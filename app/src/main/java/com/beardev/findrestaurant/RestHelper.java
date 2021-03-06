package com.beardev.findrestaurant;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jean on 11/06/2016.
 */
public class RestHelper {

    private RestApi api;

    public RestHelper () {

        api = new Retrofit.Builder()
                .baseUrl("https://find-restaurant-jeanwill.c9users.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RestApi.class);
    }

    public void listPhotos(Integer restaurantId, final GenericResultReceiver receiver) {

        final Bundle b = new Bundle();
        receiver.send(GenericResultReceiver.RUNNING, Bundle.EMPTY);

        Call<JsonArray> call = api.listPhotos(restaurantId);
        call.enqueue(new Callback<JsonArray>() {

            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                b.putSerializable("response", response.body().toString());
                receiver.send(GenericResultReceiver.LIST_PHOTOS, b);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e(RestHelper.class.getSimpleName(), "Falha ao comunicar com o servidor" + t.getMessage(), t);
                b.putSerializable("error", t);
                receiver.send(GenericResultReceiver.ERROR, b);
            }
        });
    }

    public void listMenus(Integer restaurantId, final GenericResultReceiver receiver) {

        final Bundle b = new Bundle();
        receiver.send(GenericResultReceiver.RUNNING, Bundle.EMPTY);

        Call<JsonArray> call = api.listMenus(restaurantId);
        call.enqueue(new Callback<JsonArray>() {

            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                b.putSerializable("response", response.body().toString());
                receiver.send(GenericResultReceiver.LIST_MENUS, b);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e(RestHelper.class.getSimpleName(), "Falha ao comunicar com o servidor" + t.getMessage(), t);
                b.putSerializable("error", t);
                receiver.send(GenericResultReceiver.ERROR, b);
            }
        });
    }

    public void listRestaurants(final GenericResultReceiver receiver) {

        final Bundle b = new Bundle();
        receiver.send(GenericResultReceiver.RUNNING, Bundle.EMPTY);

        Call<JsonArray> call = api.listRestaurants();
        call.enqueue(new Callback<JsonArray>() {

            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                b.putString("response", response.body().toString());
                receiver.send(GenericResultReceiver.LIST_ALL_RESTAURANTS, b);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e(RestHelper.class.getSimpleName(), "Falha ao comunicar com o servidor" + t.getMessage(), t);
                b.putSerializable("error", t);
                receiver.send(GenericResultReceiver.ERROR, b);
            }
        });
    }

    public void listReviews(Integer idRestaurant, final GenericResultReceiver receiver) {

        final Bundle b = new Bundle();
        receiver.send(GenericResultReceiver.RUNNING, Bundle.EMPTY);

        Call<JsonArray> call = api.listReviews(idRestaurant);
        call.enqueue(new Callback<JsonArray>() {

            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                b.putSerializable("response", response.body().toString());
                receiver.send(GenericResultReceiver.LIST_REVIEWS, b);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e(RestHelper.class.getSimpleName(), "Falha ao comunicar com o servidor" + t.getMessage(), t);
                b.putSerializable("error", t);
                receiver.send(GenericResultReceiver.ERROR, b);
            }
        });
    }

    public void sendReview(Review review, final GenericResultReceiver receiver) {
        final Bundle b = new Bundle();
        receiver.send(GenericResultReceiver.RUNNING, Bundle.EMPTY);

        Call<Integer> call = api.sendReview(review.getPlace(), review.getPrice(), review.getAttendance(),
                                            review.getFood(), review.getDescriprion(), review.getRestaurant_id(),
                                            review.getName());
        call.enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                b.putSerializable("response", response.body());
                receiver.send(GenericResultReceiver.ADD_REVIEW, b);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(RestHelper.class.getSimpleName(), "Falha ao comunicar com o servidor" + t.getMessage(), t);
                b.putSerializable("error", t);
                receiver.send(GenericResultReceiver.ERROR, b);
            }
        });
    }
}
