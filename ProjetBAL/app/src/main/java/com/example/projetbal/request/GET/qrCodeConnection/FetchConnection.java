package com.example.projetbal.request.GET.qrCodeConnection;

import android.os.AsyncTask;
import org.json.JSONObject;

public class FetchConnection extends AsyncTask<String, Void, String> {
    private String urlConnection;

    public FetchConnection() {
        this.urlConnection = "";
    }

    public String getUrlConnection(){ return this.urlConnection; }
    @Override
    protected String doInBackground(String... strings) {
        return QRCodeNetworkUtils.getCoonectionInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            String u = null;
            u = jsonObject.getString("urlConnection");
            if (u != null ) {
                this.urlConnection = u;
            } else {
                this.urlConnection = "NO DATA";
            }
        } catch (Exception e) {
        }
    }
}

