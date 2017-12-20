package com.example.myapplication.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myapplication.bean.BeanBrand;
import com.example.myapplication.bean.BeanBrandList;
import com.example.myapplication.database.Brand;
import com.example.myapplication.interfaces.CallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NetworkStateChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                System.out.println("BEAN:" + Brand.getBrand(context).getAllIdsStatus());
                saveData(context, Brand.getBrand(context).getAllIdsStatus());
            }
        }
    }

    private void saveData(final Context context, final ArrayList<BeanBrand> brands) {
        Request request = new Request("http://appsdata2.cloudapp.net/demo/androidApi/insert.php");
        request.setMethod(Request.Method.POST);
        String value = getJsonString(brands);
        if (value == null)
            return;
        HashMap<String, String> datas = new HashMap<>();
        datas.put("data", value);
        request.setParams(datas);

        new JsonResponse(request, BeanBrandList.class, new Network.Response() {
            @Override
            public void onComplete(Object in, String method) {
                if (in == null)
                    return;
                BeanBrandList list = (BeanBrandList) in;
                if (list.getErrorCode() == 1) {
                    for (BeanBrand brand : brands) {
                        Brand.getBrand(context).deleteRecord(brand.getPrimaryId());
                    }
                    if (context instanceof CallBack)
                        ((CallBack) context).onListUpdate();
                }
            }
        }).execute();
    }

    private String getJsonString(ArrayList<BeanBrand> brands) {
        try {
            JSONObject object = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject brand = new JSONObject();
            for (BeanBrand beanBrand : brands) {
                brand.put("name", beanBrand.getName());
                brand.put("description", beanBrand.getDescription());
                array.put(brand);
            }
            object.put("brand", array);
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}