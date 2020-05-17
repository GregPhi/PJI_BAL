package com.example.projetlivretestrequest;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_request);
        obj1 = findViewById(R.id.obj1);
        obj2 = findViewById(R.id.obj2);
        obj3 = findViewById(R.id.obj3);
    }

    public void sendRequest(View view){
        if(!TextUtils.isEmpty(obj1.getText().toString()) && !TextUtils.isEmpty(obj2.getText().toString()) && !TextUtils.isEmpty(obj3.getText().toString())){
            // https://stackoverflow.com/questions/33573803/how-to-send-a-post-request-using-volley-with-string-body
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
