package com.heremap.heremapfragment.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.heremap.heremapfragment.R;
import com.heremap.heremapfragment.product.Product;

import java.util.List;

public class AdapterProduct extends PagerAdapter {
    Context context;
    List<Product> products;
    IAdapterClick.IAdapterProductClick adapterProductClick;
    public AdapterProduct(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setAdapterProductClick(IAdapterClick.IAdapterProductClick adapterProductClick) {
        this.adapterProductClick = adapterProductClick;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product, container, false);
        Product product = products.get(position);
        TextView nameP, typeP, addressP, length, time;
        LinearLayout btnRoute;
        nameP = view.findViewById(R.id.nameProduct);
        typeP = view.findViewById(R.id.typeProduct);
        addressP = view.findViewById(R.id.addressProduct);
        btnRoute = view.findViewById(R.id.btnRoute);
        nameP.setText(product.getName());
        switch (product.getCode()) {
            case 0:
                typeP.setText("Cơm phần");
                break;
            case 1:
                typeP.setText("Bún phở");
                break;
            case 2:
                typeP.setText("Ăn vặt");
                break;
            case 3:
                typeP.setText("Trà sữa");
                break;
        }
        addressP.setText(product.getAddress());
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterProductClick.onClickArrowButton(position);
            }
        });
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
