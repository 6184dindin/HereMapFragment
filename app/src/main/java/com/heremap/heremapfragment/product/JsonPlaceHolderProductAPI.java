package com.heremap.heremapfragment.product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderProductAPI {
    @GET("comphan")
    Call<List<Product>> getRices();
    @GET("trasua")
    Call<List<Product>> getMilkTeas();
    @GET("anvat")
    Call<List<Product>> getSnacks();
    @GET("bunpho")
    Call<List<Product>> getNoodles();
}
