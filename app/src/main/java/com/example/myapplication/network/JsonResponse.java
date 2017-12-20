package com.example.myapplication.network;


import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonResponse extends Network {

    private Object object;
    private Gson gson;
    private Class aClass;
    private Response response;

    public JsonResponse(Request request, Response response) {
        this(request, null, response);
    }

    public JsonResponse(Request request, Class aClass, Response response) {
        super(request);
        this.gson = new Gson();
        this.aClass = aClass;
        this.response = response;
    }


    @Override
    public void doBackground(InputStream in) {
        try {
            String data = getString(in);
            if (data == null) return;
            if (aClass != null)
                object = gson.fromJson(data, aClass);
            else
                object = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (response != null)
            response.onComplete(object, request.getTag());
    }

    private String getString(InputStream inputStream) throws IOException {
        if (inputStream == null) return null;
        StringBuffer textInfo = new StringBuffer();
        BufferedInputStream is = new BufferedInputStream(inputStream);
        int data;
        while ((data = is.read()) != -1)
            textInfo.append((char) data);
        is.close();
        inputStream.close();
        return textInfo.toString();
    }
}
