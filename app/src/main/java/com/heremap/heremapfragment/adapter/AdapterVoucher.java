package com.heremap.heremapfragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.heremap.heremapfragment.Define;
import com.heremap.heremapfragment.R;
import com.heremap.heremapfragment.product.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterVoucher extends PagerAdapter {
    Context context;
    List<Product> products;
    IAdapterClick.IAdapterVoucherClick iAdapterVoucherClick;
    public AdapterVoucher(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setiAdapterVoucherClick(IAdapterClick.IAdapterVoucherClick iAdapterVoucherClick) {
        this.iAdapterVoucherClick = iAdapterVoucherClick;
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
        View view = inflater.inflate(R.layout.item_voucher, container, false);
        Product product = products.get(position);
        ImageView imageView = view.findViewById(R.id.imgVoucher);
        Picasso.with(context).load(Define.urlImage + product.getVoucher() + ".jpg").into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iAdapterVoucherClick.onClickImage(position);
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
