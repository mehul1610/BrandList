package com.example.myapplication.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.bean.BeanBrand;
import com.example.myapplication.bean.BeanBrandList;
import com.example.myapplication.database.Brand;
import com.example.myapplication.network.JsonResponse;
import com.example.myapplication.network.Network;
import com.example.myapplication.network.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class RegisterDialog extends Dialog implements View.OnClickListener {
    private EditText name;
    private EditText description;

    public RegisterDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    private boolean isEmpty(TextView view) {
        return view == null || view.getText().toString().trim().isEmpty();
    }

    private String getText(TextView view) {
        return isEmpty(view) ? "" : view.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        if (isEmpty(name) || isEmpty(description)) {
            Toast.makeText(getContext(), "Please Fill All Fields", Toast.LENGTH_LONG).show();
            return;
        }
        Brand.getBrand(getContext()).addBrand(new BeanBrand(getText(name), getText(description)));
        System.out.println("BEAN:" + Brand.getBrand(getContext()).getAllIdsStatus());
        saveData(Brand.getBrand(getContext()).getAllIdsStatus());
    }

    private void saveData(final ArrayList<BeanBrand> brands) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading..., Please Wait");
        dialog.show();

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
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (in == null) {
                    Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                    return;
                }
                BeanBrandList list = (BeanBrandList) in;
                if (list.getErrorCode() == 1) {
                    for (BeanBrand brand : brands) {
                        Brand.getBrand(getContext()).deleteRecord(brand.getPrimaryId());
                    }
                }
                dismiss();
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
