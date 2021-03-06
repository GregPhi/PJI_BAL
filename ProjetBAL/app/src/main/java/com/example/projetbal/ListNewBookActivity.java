package com.example.projetbal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.projetbal.dataB.book.BookViewModel;
import com.example.projetbal.dataB.matiere.MatiereViewModel;
import com.example.projetbal.listadapter.NewBookListAdapter;
import com.example.projetbal.object.Constantes;
import com.example.projetbal.object.token.Token;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.request.VolleySingleton;
import com.example.projetbal.request.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListNewBookActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        RecyclerView recyclerView = findViewById(R.id.newBook);
        final NewBookListAdapter adapter = new NewBookListAdapter(ListNewBookActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton scanNewBook = findViewById(R.id.scanNewBook);
        scanNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListNewBookActivity.this,NewBookActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton uploadNewBook = findViewById(R.id.uploadNewBook);
        uploadNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    List<Livre> books = mBookViewModel.getmAllBooksForJson();
                    JSONObject json = new JSONObject();
                    int cpt = 0;
                    while(cpt < books.size()){
                        JSONObject object = new JSONObject();
                        try {
                            object.put("code_barre",books.get(cpt).getCode_barre());
                            object.put("titre",books.get(cpt).getTitle());
                            object.put("matiere",books.get(cpt).getMatiere());
                            object.put("infos",books.get(cpt).getDescription());
                            object.put("annee",books.get(cpt).getAnnee());
                            object.put("editeur",books.get(cpt).getEditeur());
                            object.put("etat",books.get(cpt).getEtats());
                            object.put("commentaire",books.get(cpt).getCommenataires());
                            object.put("statut",books.get(cpt).getStatuts());
                            json.put(String.valueOf(cpt),object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cpt++;
                    }
                    String URL = "http://"+Token.ip+"/createLivreFromAppli";
                    postResponse(ListNewBookActivity.this,Request.Method.POST, URL, json, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            mBookViewModel.deleteAll();
                            finish();
                        }
                        @Override
                        public void onSuccessResponse(JSONObject result) {
                            return ;
                        }
                    });
                } else {
                    Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                    net.show();
                }
            }
        });

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        mBookViewModel.getmAllBooks().observe(this, new Observer<List<Livre>>() {
            @Override
            public void onChanged(@Nullable final List<Livre> book) {
                // Update the cached copy of the words in the adapter.
                adapter.setBooks(book);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == Constantes.INFO_BOOK_OK){
            Livre book = intent.getParcelableExtra("livre");
            mBookViewModel.insert(book);
        }
        if(requestCode == Constantes.INFO_BOOK_FAIL){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un code barre", Toast.LENGTH_LONG).show();
        }
    }

    public void postResponse(Context ctx, int method, String url, JSONObject jsonValue, final VolleyCallback callback) {
        RequestQueue requestQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        final String requestBody = jsonValue.toString();
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccessResponse(response);
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
            public byte[] getBody() {
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
        //stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public void removeBook(Livre current){
        mBookViewModel.delete(current);
    }

    public void infosBook(Livre current){
        Intent intent = new Intent( ListNewBookActivity.this, InfoBookActivity.class);
        intent.putExtra("livre",current);
        //mBookViewModel.delete(current);
        startActivityForResult(intent, Constantes.INFO_BOOK_ACTIVITY);
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
