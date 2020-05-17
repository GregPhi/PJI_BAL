package com.example.projetlivre.GET;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLivres extends AsyncTask<String,Void,String> {
    //https://blog.codavel.com/how-to-integrate-httpurlconnection
    @Override
    protected String doInBackground(String... params) {

        String data = null;

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            int code = httpURLConnection.getResponseCode();
            if(code != 200){
                throw new IOException("Invalid response from server : "+code);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            //https://stackoverflow.com/questions/26358684/converting-bufferedreader-to-jsonobject-or-map

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}

