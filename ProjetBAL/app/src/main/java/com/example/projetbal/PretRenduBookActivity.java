package com.example.projetbal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.projetbal.dataB.found.FoundBookViewModel;
import com.example.projetbal.listadapter.PretRenduBookListAdapter;
import com.example.projetbal.object.Constantes;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.enumO.StatutsLivre;
import com.example.projetbal.object.token.Token;
import com.example.projetbal.request.VolleySingleton;
import com.example.projetbal.request.VolleyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PretRenduBookActivity extends AppCompatActivity {

    public static FoundBookViewModel mFoundBookViewModel;
    private String user;
    private String methode;
    private static String commentaireD;
    private final String URL_POST = "http://"+Token.ip+"/statutLivre";
    private final String URL_GET = "http://"+Token.ip+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret_rendu_book);
        Toolbar toolbar = findViewById(R.id.statutToolbar);
        setSupportActionBar(toolbar);
        commentaireD = "";

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
                if (isConnected()) {
                    List<FoundLivre> books = mFoundBookViewModel.getmAllFoundBooksList();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Commentaires",commentaireD);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int cpt = 0;
                    while(cpt < books.size()){
                        JSONObject object = new JSONObject();
                        try {
                            Livre book = books.get(cpt).getLivre();
                            object.put("code_barre",book.getCode_barre());
                            object.put("titre",book.getTitle());
                            object.put("matiere",book.getMatiere());
                            object.put("infos",book.getDescription());
                            object.put("annee",book.getAnnee());
                            object.put("editeur",book.getEditeur());
                            object.put("etat",book.getEtats());
                            object.put("commentaire",book.getCommenataires());
                            object.put("statut",book.getStatuts());
                            json.put(String.valueOf(cpt),object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cpt++;
                    }
                    postResponse(PretRenduBookActivity.this,Request.Method.POST, URL_POST, json, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            mFoundBookViewModel.deleteAll();
                            //finish();
                            getResponse(PretRenduBookActivity.this,Request.Method.GET, URL_GET, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    return ;
                                }
                                @Override
                                public void onSuccessResponse(JSONObject result) {
                                    try {
                                        int cpt = 0;
                                        JSONArray array = result.getJSONArray("livres");
                                        while(cpt < array.length()){
                                            JSONObject obj = array.getJSONObject(cpt);
                                            Livre livre = new Livre();
                                            livre.setCode_barre(obj.getString("code barre"));
                                            livre.setTitle(obj.getString("titre"));
                                            livre.setMatiere(obj.getString("matiere"));
                                            livre.setDescription(obj.getString("infos"));
                                            livre.setAnnee(obj.getString("annee"));
                                            livre.setEditeur(obj.getString("editeur"));
                                            livre.setEtats(obj.getString("etat du livre"));
                                            livre.setCommenataires(obj.getString("commentaire"));
                                            livre.setStatuts(obj.getString("statut"));
                                            mFoundBookViewModel.insert(new FoundLivre(livre));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
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

        mFoundBookViewModel.getmAllFoundBooks().observe(this, new Observer<List<FoundLivre>>() {
            @Override
            public void onChanged(@Nullable final List<FoundLivre> book) {
                // Update the cached copy of the words in the adapter.
                adapter.setFoundBooks(book);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_statut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_isbn){
            AlertDialog dialog = setDialogBox("Rentrez un ISBN que vous recherchez","Recherchez");
            dialog.show();
            return true;
        }
        if (id == R.id.action_commentaire){
            AlertDialog dialog = setDialogBox("Création d'un commentaire","Création");
            dialog.show();
            return true;
        }
        if (id == R.id.action_eleveid){
            AlertDialog dialog = setDialogBox("Rentrez l'ID d'un élève","Recherchez");
            dialog.show();
            return true;
        }
        if (id == R.id.action_user){
            AlertDialog dialog = setDialogBox("Entrez un identifiant","Recherchez");
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String isbn = scanningResult.getContents();
            serachIsbnInFoundBook(isbn);
        }
        if (scanningResult == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(requestCode == Constantes.INFO_BOOK_OK){
            FoundLivre book = intent.getParcelableExtra("livre");
            mFoundBookViewModel.insert(book);
        }
        if(requestCode == Constantes.INFO_BOOK_FAIL){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un code barre", Toast.LENGTH_LONG).show();
        }
    }

    public AlertDialog setDialogBox(final String t, String v){
        final AlertDialog dialog = new AlertDialog.Builder(PretRenduBookActivity.this).create();
        LayoutInflater layout = PretRenduBookActivity.this.getLayoutInflater();
        View dialogView = layout.inflate(R.layout.dialog_input,null);
        TextView title = dialogView.findViewById(R.id.dialogTitle);
        title.setText(t);
        final EditText input = dialogView.findViewById(R.id.inputDialog);
        Button annuler = dialogView.findViewById(R.id.buttonAnnulate);
        Button ok = dialogView.findViewById(R.id.buttonValidate);
        ok.setText(v);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(input.getText())){
                    String in = input.getText().toString();
                    switch (t){
                        case "Création d'un commentaire":
                            PretRenduBookActivity.commentaireD += input.getText().toString()+"\n";
                            break;
                        case "Rentrez un ISBN que vous recherchez":
                            serachIsbnInFoundBook(input.getText().toString());
                            break;
                        case "Rentrez l'ID d'un élève":
                            RequestQueue requestQueueID = VolleySingleton.getInstance(PretRenduBookActivity.this).getRequestQueue();
                            StringRequest stringRequestID = new StringRequest(Request.Method.GET, URL_GET+"getLivresEleve/"+in, new Response.Listener<String>() {
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
                            VolleySingleton.getInstance(PretRenduBookActivity.this).addToRequestQueue(stringRequestID);
                            break;
                        case "Entrez un identifiant":
                            RequestQueue requestQueueUser = VolleySingleton.getInstance(PretRenduBookActivity.this).getRequestQueue();
                            StringRequest stringRequestUser = new StringRequest(Request.Method.GET, URL_GET+in, new Response.Listener<String>() {
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
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("VOLLEY", error.toString());
                                }
                            });
                            VolleySingleton.getInstance(PretRenduBookActivity.this).addToRequestQueue(stringRequestUser);
                            break;
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        return dialog;
    }

    public void serachIsbnInFoundBook(String isbn){
        List<FoundLivre> fL = mFoundBookViewModel.getmAllFoundBooksList();
        for(FoundLivre f : fL){
            Livre l = f.getLivre();
            if(l.getCode_barre().equals(isbn)){
                mFoundBookViewModel.delete(f);
                String s = l.getStatuts();
                l.setStatuts(StatutsLivre.getStatutByMethod(methode));
                f.setLivre(l);
                f.setFound(true);
                mFoundBookViewModel.insert(f);
            }
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

    public void getResponse(Context ctx, int method, String url, final VolleyCallback callback) {
        RequestQueue requestQueue = VolleySingleton.getInstance(ctx).getRequestQueue();
        JsonObjectRequest getRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        VolleySingleton.getInstance(ctx).addToRequestQueue(getRequest);
    }

    public void infosBook(FoundLivre current){
        Intent intent = new Intent( PretRenduBookActivity.this, InfoBookStatutActivity.class);
        intent.putExtra("livre",current);
        startActivityForResult(intent, Constantes.INFO_BOOK_ACTIVITY);
    }

    public void bookVerified(FoundLivre current){
        Livre l = current.getLivre();
        mFoundBookViewModel.delete(current);
        String s = l.getStatuts();
        l.setStatuts(StatutsLivre.getStatutByMethod(methode));
        current.setLivre(l);
        current.setFound(true);
        mFoundBookViewModel.insert(current);
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
