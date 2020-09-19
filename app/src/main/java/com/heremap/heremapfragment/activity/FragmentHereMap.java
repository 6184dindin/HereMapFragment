package com.heremap.heremapfragment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.here.android.mpa.mapping.Map;
import com.heremap.heremapfragment.R;
import com.heremap.heremapfragment.SQLHelper;
import com.heremap.heremapfragment.adapter.AdapterProduct;
import com.heremap.heremapfragment.adapter.IAdapterClick;
import com.heremap.heremapfragment.databinding.ActivityFragmentHereMapBinding;
import com.heremap.heremapfragment.mylocation.GPSLocation;
import com.heremap.heremapfragment.presenter.FragmentHereMapView;
import com.heremap.heremapfragment.product.Product;

import java.util.ArrayList;
import java.util.List;

public class FragmentHereMap extends AppCompatActivity {
    ActivityFragmentHereMapBinding binding;
    Intent intent;

    List<Product> productList;
    FragmentHereMapView hereMapView;
    AdapterProduct adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fragment_here_map);
        intent = getIntent();
        setHereMapType();
        getListProduct();
        hereMapView = new FragmentHereMapView(this);
        hereMapView.addListMarkerProductOnMyMap(productList);
        adapterProduct = new AdapterProduct(this, productList);
        binding.btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productList.size() == 0) {
                    Toast.makeText(getBaseContext(), "Danh sách của bạn trống!", Toast.LENGTH_LONG).show();
                }else {
                    binding.btnViewList.setVisibility(View.GONE);
                    binding.viewListProduct.setAdapter(adapterProduct);
                    binding.viewListProduct.setPadding(80, 0, 80, 0);
                    hereMapView.removeMyRoute();
                    hereMapView.removeListMarkerProductOnMyMap();
                    hereMapView.addListMarkerProductOnMyMap(productList);
                    hereMapView.addMarkerProductSelected(0, true, productList.size());
                    binding.viewListProduct.setVisibility(View.VISIBLE);
                    binding.btnOffViewList.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.btnOffViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hereMapView.removeListMarkerProductOnMyMap();
                hereMapView.addListMarkerProductOnMyMap(productList);
                hereMapView.setMyLocation();
                binding.btnOffViewList.setVisibility(View.GONE);
                binding.viewListProduct.setVisibility(View.GONE);
                binding.btnViewList.setVisibility(View.VISIBLE);
            }
        });
        binding.viewListProduct.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                hereMapView.addMarkerProductSelected(position, true, productList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        adapterProduct.setAdapterProductClick(new IAdapterClick.IAdapterProductClick() {
            @Override
            public void onClickArrowButton(int position) {
                hereMapView.addMyRoute(productList.get(position));
                hereMapView.removeListMarkerProductOnMyMap();
                hereMapView.addMarkerProductSelected(position, false, productList.size());
                binding.viewListProduct.setVisibility(View.GONE);
                binding.btnOffViewList.setVisibility(View.GONE);
                binding.btnViewList.setVisibility(View.VISIBLE);
            }
        });
        binding.btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hereMapView.setMyLocation();
            }
        });
    }

    private void getListProduct() {
        productList = new ArrayList<>();
        for (int i = 0; i < intent.getIntExtra("size_list", 0); i++) {
            String index = String.valueOf(i);
            Product product = (Product) intent.getSerializableExtra(index);
            productList.add(product);
        }
    }

    void setHereMapType() {
        binding.btnSetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.layoutSetMap.getVisibility() == View.GONE) {
                    binding.layoutSetMap.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutSetMap.setVisibility(View.GONE);
                }
            }
        });
        binding.btnSetMapNormalDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hereMapView.setMapType(Map.Scheme.NORMAL_DAY);
                binding.layoutSetMap.setVisibility(View.GONE);
            }
        });
        binding.btnSetMapSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hereMapView.setMapType(Map.Scheme.HYBRID_DAY);
                binding.layoutSetMap.setVisibility(View.GONE);
            }
        });
        binding.btnSetMapNormalNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hereMapView.setMapType(Map.Scheme.NORMAL_NIGHT);
                binding.layoutSetMap.setVisibility(View.GONE);
            }
        });
    }
}