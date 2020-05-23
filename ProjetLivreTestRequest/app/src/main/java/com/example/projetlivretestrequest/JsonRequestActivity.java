package com.example.projetlivretestrequest;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class JsonRequestActivity extends AppCompatActivity {
    private EditText obj1;
    private EditText obj2;
    private EditText obj3;

    private TextView get1;
    private TextView get2;
    private TextView get3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_request);

        obj1 = findViewById(R.id.obj1);
        obj2 = findViewById(R.id.obj2);
        obj3 = findViewById(R.id.obj3);

        get1 = findViewById(R.id.get1);
        get2 = findViewById(R.id.get2);
        get3 = findViewById(R.id.get3);
    }


    public void getRequest(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://localhost:8080/livre/JSON";
        // prepare the Request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        get1.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                get1.setText("That didn't work!");
            }
        });

        // add it to the RequestQueue
        queue.add(stringRequest);
    }

    public void postRequest(View view) throws IOException {
        System.setProperty("http.keepAlive", "false");
        if(!TextUtils.isEmpty(obj1.getText().toString())){
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                boolean wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                Log.d("NetworkState", "L'interface de connexion active est du Wifi : " + wifi);
                HttpCall httpCallPost = new HttpCall();
                httpCallPost.setMethodtype(HttpCall.POST);
                httpCallPost.setUrl("http://192.168.1.41:8080/test");
                //HashMap<String,String> paramsPost = new HashMap<>();
                //paramsPost.put("name","Julius Cesar");
                httpCallPost.setParams(obj1.getText().toString());
                new HttpRequest(){
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        obj2.setText(obj2.getText()+"\nPost:"+response);
                    }
                }.execute(httpCallPost);
                /*URL obj = new URL("http://localhost:8080/test");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                // For POST only - START
                con.setDoOutput(true);
                OutputStream os = con.getOutputStream();
                os.write(obj1.getText().toString().getBytes());
                os.flush();
                os.close();
                // For POST only - END

                int responseCode = con.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);

                if (responseCode != HttpURLConnection.HTTP_OK) { //success
                    System.out.println("POST request not worked");
                }*/
            }

            /*try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                final String url = "http://127.0.0.1:8080/test";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("obj1", obj1.getText().toString());
                jsonBody.put("obj1", obj2.getText().toString());
                jsonBody.put("obj1", obj3.getText().toString());
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }

                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> mParams = new HashMap<String, String>();
                        mParams.put("username", "sa");
                        return mParams;
                    }
                };
                stringRequest.setShouldCache(false);
                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }
}
