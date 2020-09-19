package com.heremap.heremapfragment.presenter;

import com.heremap.heremapfragment.product.Product;

import java.util.List;

public interface IPresenter {
    interface IMainPre {
        void getRices(List<Product> products);
        void getNoodles(List<Product> products);
        void getSnacks(List<Product> products);
        void getMilkTeas(List<Product> products);
        void getProductList(List<Product> products);
    }
}
