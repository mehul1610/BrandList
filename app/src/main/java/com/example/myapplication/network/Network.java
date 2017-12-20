package com.example.myapplication.network;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public abstract class Network extends AsyncTask<Void, Integer, Void> {

    public Request request;
    private HttpURLConnection httpURLConnection;

    protected Network(Request request) {
        this.request = request;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void[] params) {
        if (request == null || request.getUrl() == null) return null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setConnectTimeout(30000);
            httpURLConnection.setUseCaches(false);
            Map<String, String> headers = request.getHeaders();
            if (headers != null)
                for (String header : headers.keySet())
                    httpURLConnection.setRequestProperty(header, headers.get(header));
            setRequestMethod(httpURLConnection, request);
            doBackground(httpURLConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            doBackground(null);
        }
        return null;
    }

    public abstract void doBackground(InputStream in);

    private void setRequestMethod(HttpURLConnection httpURLConnection, Request request) throws IOException {
        if (request.getMethod() == null) {
            httpURLConnection.setRequestMethod("GET");
            return;
        }
        switch (request.getMethod()) {
            case GET:
                httpURLConnection.setRequestMethod("GET");
                break;
            case POST:
                httpURLConnection.setRequestMethod("POST");
                addBodyIfExists(httpURLConnection, request);
        }
    }

    private void addBodyIfExists(HttpURLConnection connection, Request request)
            throws IOException {
        byte[] body = addBody(request.getParams());
        if (body != null) {
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private byte[] addBody(Map<String, String> params) {
        if (params == null || params.size() < 1) return null;
        StringBuilder body = new StringBuilder();
        try {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    body.append("&");
                if (entry.getKey().equals("JSON_DATA")) {
                    body.append(entry.getValue());
                    continue;
                }
                body.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                body.append("=");
                body.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding UTF-8");
        }
        return body.toString().getBytes();
    }

    public interface Response {
        void onComplete(Object in, String method);
    }
}
