package com.example.projetbal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetbal.GET.qrCodeConnection.FetchConnection;
import com.example.projetbal.dataB.book.BookViewModel;
import com.example.projetbal.dataB.found.FoundBookViewModel;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;
    public static FoundBookViewModel mFoundBookViewModel;
    private static String urlConnection = "";
    private EditText userEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userEdit = findViewById(R.id.userMain);

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        mFoundBookViewModel = ViewModelProviders.of(this).get(FoundBookViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected() && urlConnection.length() != 0) {
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
            final String[] methode = {"test"};
            /*RequestQueue queue = Volley.newRequestQueue(this);
            final String url = ""+user;
            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                methode[0] = response.getString("methode");
                                int cpt = 0;
                                JSONArray array = response.getJSONArray("livres");
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
                                    mBookViewModel.insert(livre);
                                    mFoundBookViewModel.insert(new FoundLivre(livre.getCode_barre()));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            queue.add(getRequest);*/
            Intent intent = new Intent(MainActivity.this,PretRenduBookActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("methode", methode[0]);
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
}
