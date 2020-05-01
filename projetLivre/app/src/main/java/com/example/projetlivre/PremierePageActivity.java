package com.example.projetlivre;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetlivre.GET.qrCodeConnection.FetchConnection;
import com.example.projetlivre.GET.qrCodeConnection.QRCodeLoader;
import com.example.projetlivre.GET.qrCodeConnection.QRCodeNetworkUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class PremierePageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static String urlConnection = "";
    private final static String[] users = {"user1","user2"};
    private EditText numUser;
    private Spinner choix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        this.numUser = findViewById(R.id.numUser);
        this.choix = findViewById(R.id.choix);
    }

    public void validate(View view){
        if (TextUtils.isEmpty(numUser.getText())){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un nom d'utilisateur", Toast.LENGTH_LONG).show();
        }else{
            boolean b = false;
            String user = numUser.getText().toString();
            for(String u : users) {
                if(user.equals(u)){
                    b = true;
                    continue;
                }
            }
            if(b){
                String c = choix.getSelectedItem().toString();
                this.choiceMethode(c);
            }else{
                Toast.makeText(getApplicationContext(), "Nom d'utilisateur faux", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new QRCodeLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
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
                String m = f.getMethode();
                this.choiceMethode(m);
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

    public void connection(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void choiceMethode(String c){
        switch(c) {
            case "Prêt de livre(s)":
                break;
            case "Rendu de livre(s)":
                break;
            case "MAJ livres":
                Intent majBook = new Intent(PremierePageActivity.this, MiseAJourBookActivity.class);
                startActivity(majBook);
                break;
            default:
                Intent newBook = new Intent(PremierePageActivity.this, NewBookActivity.class);
                startActivity(newBook);
                break;
        }
    }
}
