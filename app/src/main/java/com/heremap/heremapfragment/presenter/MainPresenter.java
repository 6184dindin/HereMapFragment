package com.heremap.heremapfragment.presenter;

import android.content.Context;

import com.heremap.heremapfragment.Define;
import com.heremap.heremapfragment.SQLHelper;
import com.heremap.heremapfragment.product.Product;
import com.heremap.heremapfragment.product.JsonPlaceHolderProductAPI;
import com.heremap.heremapfragment.product.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    Context context;
    IPresenter.IMainPre iMainPre;
    List<Product> products;
    List<Product> rices;
    List<Product> milkTeas;
    List<Product> noodles;
    List<Product> snacks;
    SQLHelper sqlHelper;
    JsonPlaceHolderProductAPI jsonPlaceHolderProductAPI;

    public MainPresenter(Context context, IPresenter.IMainPre iMainPre) {
        this.context = context;
        this.iMainPre = iMainPre;
        this.sqlHelper = new SQLHelper(context);
    }

    public void getAllProduct() {
        products = new ArrayList<>();
        rices = new ArrayList<>();
        noodles = new ArrayList<>();
        snacks = new ArrayList<>();
        milkTeas = new ArrayList<>();
        getProductJson(0);
        getProductJson(1);
        getProductJson(2);
        getProductJson(3);
        products = sqlHelper.getAllProduct();
        for (Product product : products) {
            if (product.getCode() == 0) {
                rices.add(product);
            }
            if (product.getCode() == 1) {
                noodles.add(product);
            }
            if (product.getCode() == 2) {
                snacks.add(product);
            }
            if (product.getCode() == 3) {
                milkTeas.add(product);
            }
        }
        iMainPre.getRices(rices);
        iMainPre.getNoodles(noodles);
        iMainPre.getSnacks(snacks);
        iMainPre.getMilkTeas(milkTeas);
        iMainPre.getProductList(products);
    }

    private void getProductJson(int typeProduct) {
        jsonPlaceHolderProductAPI = RetrofitClient.getClient(Define.urlAPI).create(JsonPlaceHolderProductAPI.class);
        Call<List<Product>> call = null;
        switch (typeProduct) {
            case 0:
                call = jsonPlaceHolderProductAPI.getRices();
                break;
            case 1:
                call = jsonPlaceHolderProductAPI.getNoodles();
                break;
            case 2:
                call = jsonPlaceHolderProductAPI.getSnacks();
                break;
            case 3:
                call = jsonPlaceHolderProductAPI.getMilkTeas();
                break;
            default:
                break;
        }
        callJson(call, typeProduct);
    }

    private void callJson(Call<List<Product>> call, int typeProduct) {
            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    List<Product> products = response.body();
                    if (typeProduct == 0) {
                        sqlHelper.onDeleteAll();
                    }
                    for (int i = 0; i < products.size(); i++) {
                        sqlHelper.insertProduct(typeProduct, products.get(i));
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                }
            });
    }
}
