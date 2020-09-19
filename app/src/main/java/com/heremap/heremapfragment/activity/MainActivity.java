package com.heremap.heremapfragment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.heremap.heremapfragment.presenter.IPresenter;
import com.heremap.heremapfragment.R;
import com.heremap.heremapfragment.adapter.AdapterVoucher;
import com.heremap.heremapfragment.adapter.IAdapterClick;
import com.heremap.heremapfragment.databinding.ActivityMainBinding;
import com.heremap.heremapfragment.presenter.MainPresenter;
import com.heremap.heremapfragment.product.Product;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements IPresenter.IMainPre {
    ActivityMainBinding binding;

    List<Product> riceList;
    List<Product> milkTeaList;
    List<Product> noodleList;
    List<Product> snackList;
    List<Product> productList;
    AdapterVoucher adapterVoucherRices;
    AdapterVoucher adapterVoucherMilkTeas;
    AdapterVoucher adapterVoucherSnacks;
    AdapterVoucher adapterVoucherProducts;

    MainPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (!checkRequiredPermission()) {
            checkRequiredPermission();
        }
        presenter = new MainPresenter(this, this);
        presenter.getAllProduct();
        adapterVoucherProducts = new AdapterVoucher(this, productList);
        binding.viewPagerProducts.setAdapter(adapterVoucherProducts);
        binding.viewPagerProducts.setPadding(130, 0, 130,0);
        adapterVoucherProducts.setiAdapterVoucherClick(new IAdapterClick.IAdapterVoucherClick() {
            @Override
            public void onClickImage(int position) {
                List<Product> products = new ArrayList<>();
                products.add(productList.get(position));
                callActivity(products);
            }
        });
        adapterVoucherRices = new AdapterVoucher(this, riceList);
        binding.viewPagerRices.setAdapter(adapterVoucherRices);
        binding.viewPagerRices.setPadding(130, 0, 130,0);
        adapterVoucherRices.setiAdapterVoucherClick(new IAdapterClick.IAdapterVoucherClick() {
            @Override
            public void onClickImage(int position) {
                List<Product> products = new ArrayList<>();
                products.add(riceList.get(position));
                callActivity(products);
            }
        });
        binding.btnRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(riceList);
            }
        });
        binding.btnNoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(noodleList);
            }
        });
        adapterVoucherSnacks = new AdapterVoucher(this, snackList);
        binding.viewPagerSnacks.setAdapter(adapterVoucherSnacks);
        binding.viewPagerSnacks.setPadding(270, 0, 270,0);
        adapterVoucherSnacks.setiAdapterVoucherClick(new IAdapterClick.IAdapterVoucherClick() {
            @Override
            public void onClickImage(int position) {
                List<Product> products = new ArrayList<>();
                products.add(snackList.get(position));
                callActivity(products);
            }
        });
        binding.btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(snackList);
            }
        });
        adapterVoucherMilkTeas = new AdapterVoucher(this, milkTeaList);
        binding.viewPagerMilkTeas.setAdapter(adapterVoucherMilkTeas);
        binding.viewPagerMilkTeas.setPadding(270, 0, 270,0);
        adapterVoucherMilkTeas.setiAdapterVoucherClick(new IAdapterClick.IAdapterVoucherClick() {
            @Override
            public void onClickImage(int position) {
                List<Product> products = new ArrayList<>();
                products.add(milkTeaList.get(position));
                callActivity(products);
            }
        });
        binding.btnMilkTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callActivity(milkTeaList);
            }
        });
    }
    private void callActivity(List<Product> products) {
        if(products.size() > 0) {
            Intent intent = new Intent(getBaseContext(), FragmentHereMap.class);
            for (int i = 0; i < products.size(); i++) {
                intent.putExtra(String.valueOf(i), products.get(i));
            }
            intent.putExtra("size_list", products.size());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), "Danh sách của bạn trống!", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkRequiredPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.WAKE_LOCK};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.mess_request_permission),
                    20000, perms);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void getRices(List<Product> products) {
        riceList = products;
    }

    @Override
    public void getNoodles(List<Product> products) {
        noodleList = products;
    }

    @Override
    public void getSnacks(List<Product> products) {
        snackList = products;
    }

    @Override
    public void getMilkTeas(List<Product> products) {
        milkTeaList = products;
    }

    @Override
    public void getProductList(List<Product> products) {
        productList = products;
    }
}