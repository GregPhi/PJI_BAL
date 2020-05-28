package com.example.projetbal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetbal.GET.qrCodeConnection.FetchConnection;
import com.example.projetbal.dataB.book.BookViewModel;
import com.example.projetbal.dataB.found.FoundBookViewModel;
import com.example.projetbal.object.Constantes;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

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
        if(resultCode==Constantes.NEW_BOOK_OK){

        }
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
            String methode = "";
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isConnected()) {
                //GetBookList get = new GetBookList("");
                //get.getBook(user,mBookViewModel,mFoundBookViewModel);
                //methode = get.getMethode();
            } else {
                Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                net.show();
            }
            Intent intent = new Intent(MainActivity.this,PretRenduBookActivity.class);
            intent.putExtra("user", user);
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
        startActivityForResult(intent, Constantes.NEW_BOOK_ACTIVITY);
    }
}
