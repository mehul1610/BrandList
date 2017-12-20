package com.example.myapplication.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.myapplication.bean.BeanBrandList;
import com.example.myapplication.database.Brand;
import com.example.myapplication.network.JsonResponse;
import com.example.myapplication.network.Network;
import com.example.myapplication.network.Request;

public class FetchService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listUpdate();
    }

    private void listUpdate() {
        Request request = new Request("http://appsdata2.cloudapp.net/demo/androidApi/list.php");

        new JsonResponse(request, BeanBrandList.class, new Network.Response() {
            @Override
            public void onComplete(Object in, String method) {
                if (in == null)
                    return;
                BeanBrandList list = (BeanBrandList) in;
                if (list.getErrorCode() == 1 && list.getBrandList() != null) {
                    Brand.getBrand(getBaseContext()).addBrands(((BeanBrandList) in).getBrandList());
                }
            }
        }).execute();
    }
}
