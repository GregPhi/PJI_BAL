package com.example.projetbal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.example.projetbal.POST.SendBookList;
import com.example.projetbal.dataB.found.FoundBookViewModel;
import com.example.projetbal.listadapter.PretRenduBookListAdapter;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.enumO.StatutsLivre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class PretRenduBookActivity extends AppCompatActivity {

    public static FoundBookViewModel mFoundBookViewModel;
    private String user;
    private String methode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret_rendu_book);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        methode = intent.getStringExtra("methode");

        mFoundBookViewModel = ViewModelProviders.of(this).get(FoundBookViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.pret_renduBook);
        final PretRenduBookListAdapter adapter = new PretRenduBookListAdapter(PretRenduBookActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton scanQRPretRendu = findViewById(R.id.scanQRPretRendu);
        scanQRPretRendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(PretRenduBookActivity.this);
                scanIntegrator.initiateScan();
            }
        });

        FloatingActionButton sendPretRendu = findViewById(R.id.sendPretRendu);
        sendPretRendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if (connMgr != null) {
                    networkInfo = connMgr.getActiveNetworkInfo();
                }
                if (networkInfo != null && networkInfo.isConnected()) {
                    for(FoundLivre l : mFoundBookViewModel.getmAllFoundBooksList()){
                        if(l.getFound()){
                            Livre livre = l.getLivre();
                            String statut = StatutsLivre.getNextStatut(livre.getStatuts());
                            livre.setStatuts(statut);
                            l.setLivre(livre);
                            mFoundBookViewModel.insert(l);
                        }
                    }
                    List<Livre> books = null;// mBookViewModel.getmAllBooksForJson();
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
                    SendBookList send = new SendBookList("",json);
                    send.sendRequest();
                } else {
                    Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                    net.show();
                }
            }
        });

        mFoundBookViewModel.getmAllFoundBooks().observe(this, new Observer<List<FoundLivre>>() {
            @Override
            public void onChanged(@Nullable final List<FoundLivre> book) {
                // Update the cached copy of the words in the adapter.
                adapter.setFoundBooks(book);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            List<FoundLivre> fL = mFoundBookViewModel.getmAllFoundBooksList();
            for(FoundLivre f : fL){
                if(f.getLivre().getCode_barre().equals(scanContent)){
                    mFoundBookViewModel.delete(f);
                    f.setFound(true);
                    mFoundBookViewModel.insert(f);
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void send(){
        RequestQueue requestQueue = Volley.newRequestQueue(PretRenduBookActivity.this);
        String URL = "";
        List<FoundLivre> foundLivres = mFoundBookViewModel.getmAllFoundBooksList();
        JSONObject json = new JSONObject();
        int cpt = 0;
        while(cpt < foundLivres.size()){
            Livre livre = foundLivres.get(cpt).getLivre();
            JSONObject object = new JSONObject();
            try {
                object.put("code_barre",livre.getCode_barre());
                object.put("titre",livre.getTitle());
                object.put("matiere",livre.getMatiere());
                object.put("infos",livre.getDescription());
                object.put("annee",livre.getAnnee());
                object.put("editeur",livre.getEditeur());
                object.put("etat",livre.getEtats());
                object.put("commentaire",livre.getCommenataires());
                object.put("statut",livre.getStatuts());
                json.put(String.valueOf(cpt),object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cpt++;
        }
        final String requestBody = json.toString();
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
                    System.out.println(requestBody);
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
    }

    public void infosBook(Livre current){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Informations");
        dialog.setMessage(current.toString()+"\n"+current.getStatuts());
        dialog.setCancelable(true);
        dialog.show();
    }
}
