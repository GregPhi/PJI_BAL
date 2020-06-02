package com.example.projetbal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.projetbal.dataB.matiere.MatiereViewModel;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.Matiere;
import com.example.projetbal.object.token.Token;
import com.example.projetbal.request.GET.qrCodeConnection.FetchConnection;
import com.example.projetbal.dataB.book.BookViewModel;
import com.example.projetbal.dataB.found.FoundBookViewModel;
import com.example.projetbal.object.Constantes;
import com.example.projetbal.request.VolleyCallback;
import com.example.projetbal.request.VolleySingleton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;
    public static FoundBookViewModel mFoundBookViewModel;
    public static MatiereViewModel matiereViewModel;
    private static String urlConnection = "";
    private EditText userEdit;
    private EditText idEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userEdit = findViewById(R.id.userMain);
        idEdit = findViewById(R.id.editId);

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        mFoundBookViewModel = ViewModelProviders.of(this).get(FoundBookViewModel.class);
        matiereViewModel = ViewModelProviders.of(this).get(MatiereViewModel.class);

        String URL = "http://"+ Token.ip+"/getMatieres";
        if(isConnected()){
            recupMatiere(MainActivity.this, Request.Method.GET, URL, new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    try {
                        JSONObject mat = new JSONObject(result);
                        Iterator<String> keys = mat.keys();
                        List<Matiere> ma = new ArrayList<>();
                        while(keys.hasNext()){
                            Matiere m = new Matiere();
                            String k = keys.next();
                            m.setNom(k);
                            m.setId((Integer) mat.get(k));
                            ma.add(m);
                        }
                        for(Matiere matt : ma){
                            matiereViewModel.insert(matt);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onSuccessResponse(JSONObject result) {
                    return ;
                }
            });
        }else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            urlConnection = scanningResult.getContents();
            if (isConnected() && urlConnection.length() != 0) {
                FetchConnection f = (FetchConnection) new FetchConnection().execute(urlConnection);
                Toast load = Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT);
                load.show();
                String urlConnect = f.getUrlConnection();
                if(!urlConnect.equals("NO DATA")){

                }else{
                    Toast url = Toast.makeText(getApplicationContext(), "URL CONNECTION NOT FOUND !", Toast.LENGTH_SHORT);
                    url.show();
                }
            } else {
                if (urlConnection.length() == 0) {
                    Toast term = Toast.makeText(getApplicationContext(), "NO SEARCH TERM !", Toast.LENGTH_SHORT);
                    term.show();
                } else {
                    Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                    net.show();
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void connectWithUser(View view){
        if(TextUtils.isEmpty(userEdit.getText().toString())){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un nom d'utilisateur", Toast.LENGTH_LONG).show();
        }else{
            String user = userEdit.getText().toString();
            String methode = "";
            if (isConnected()) {
                //GetBookList get = new GetBookList("");
                //get.getBook(user,mBookViewModel,mFoundBookViewModel);
                //methode = get.getMethode();
            } else {
                Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                net.show();
            }
            Intent intent = new Intent(MainActivity.this,PretRenduBookActivity.class);
            intent.putExtra("user", user);
            if(user.toLowerCase().equals("emprunt")){
                methode = "Emprunt";
            }
            if(user.toLowerCase().equals("rendu")){
                methode = "Rendu";
            }
            intent.putExtra("methode", methode);
            startActivity(intent);
        }
    }

    public void connectWithQRCode(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void newBookActivity(View view){
        Intent intent = new Intent(MainActivity.this,ListNewBookActivity.class);
        startActivity(intent);
    }

    public void searchLivreWithId(View view){
        if(TextUtils.isEmpty(idEdit.getText().toString())){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un nom d'utilisateur", Toast.LENGTH_LONG).show();
        }else{
            String id = idEdit.getText().toString();
            if (isConnected()) {
                recupLivresTest(MainActivity.this,Request.Method.GET, "http://"+Token.ip+"/getLivresEleve/"+id);
            } else {
                Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                net.show();
            }
            Intent intent = new Intent(MainActivity.this,PretRenduBookActivity.class);
            startActivity(intent);
        }
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

    public void recupLivresTest(Context ctx, int method, String url) {
        RequestQueue requestQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    mFoundBookViewModel.deleteAll();
                    JSONObject livres = new JSONObject(response);
                    for(int i =0; i < livres.length(); i++){
                        JSONObject livre = (JSONObject) livres.get(String.valueOf(i));
                        Livre l = new Livre();
                        l.setId(livre.getInt("id"));
                        l.setTitle(livre.getString("titre"));
                        l.setMatiere(livre.getString("matiere"));
                        l.setAnnee(livre.getString("niveau"));
                        l.setEtats(livre.getString("etat livre"));
                        l.setStatuts(livre.getString("statut livre"));
                        FoundLivre f = new FoundLivre(l);
                        mFoundBookViewModel.insert(f);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        });
        VolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public void recupMatiere(Context ctx, int method, String url, final VolleyCallback volleyCallback) {
        RequestQueue requestQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        VolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }
}
