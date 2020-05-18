package com.example.projetlivretestrequest;

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

import java.io.UnsupportedEncodingException;

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
        final String url = "http://httpbin.org/get?param1=hello";
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        String obj1 = null;
                        String obj2 = null;
                        String obj3 = null;
                        try {
                            obj1 = response.getString("get1");
                            obj2 = response.getString("get2");
                            obj3 = response.getString("get3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        get1.setText(obj1);
                        get2.setText(obj2);
                        get3.setText(obj3);
                        // display response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    public void postRequest(View view){
        if(!TextUtils.isEmpty(obj1.getText().toString()) && !TextUtils.isEmpty(obj2.getText().toString()) && !TextUtils.isEmpty(obj3.getText().toString())){
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = "http://...";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("obj1", obj1.getText().toString());
                jsonBody.put("obj1", obj2.getText().toString());
                jsonBody.put("obj1", obj3.getText().toString());
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
