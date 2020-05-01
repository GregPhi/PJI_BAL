package com.example.projetlivre.GET.qrCodeConnection;

import android.os.AsyncTask;

import org.json.JSONObject;

public class FetchConnection extends AsyncTask<String, Void, String> {
    private String user;
    private String methode;

    public FetchConnection() {
        this.user = "";
        this.methode = "";
    }

    public String getUser(){ return this.user; }
    public String getMethode(){ return this.methode; }

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
            String m = null;
            u = jsonObject.getString("user");
            m = jsonObject.getString("methode");
            if (u != null && m != null) {
                this.user = u;
                this.methode = m;
            } else {
                this.user = "NO DATA";
                this.methode = "NO DATA";
            }
        } catch (Exception e) {
        }
    }
}

