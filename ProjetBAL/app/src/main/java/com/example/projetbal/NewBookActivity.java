package com.example.projetbal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projetbal.dataB.matiere.MatiereViewModel;
import com.example.projetbal.object.book.Matiere;
import com.example.projetbal.request.GET.book.BookLoader;
import com.example.projetbal.request.GET.book.FetchBook;
import com.example.projetbal.dataB.book.BookViewModel;
import com.example.projetbal.object.book.Livre;
import com.example.projetbal.object.book.enumO.StatutsLivre;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class NewBookActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<String> {
    public static BookViewModel mBookViewModel;
    public static MatiereViewModel matiereViewModel;

    private Livre newBook = null;

    private EditText mEditISBNView;

    private EditText mEditTitleView;
    private EditText mEditCodeBarreView;
    private Spinner mSpinnerMatiere;
    private EditText mEditEditeurView;
    private EditText mEditDescriptionView;
    private Spinner mSpinnerAnnee;
    private Spinner mSpinnerEtat;
    private EditText mEditCommentaire;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_book);

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        matiereViewModel = MainActivity.matiereViewModel;

        mEditISBNView = findViewById(R.id.isbnScan);

        mEditCodeBarreView = findViewById(R.id.edit_cd);
        mEditTitleView = findViewById(R.id.edit_titre);
        mSpinnerMatiere = (Spinner) findViewById(R.id.matiere);
        mEditEditeurView = findViewById(R.id.editeur);
        mEditDescriptionView = findViewById(R.id.edit_descrip);
        mSpinnerAnnee = (Spinner) findViewById(R.id.annee);
        mSpinnerEtat = (Spinner) findViewById(R.id.etatLivre);
        mEditCommentaire = findViewById(R.id.commentaire);

        ArrayList<String> matieres = (ArrayList<String>) matiereViewModel.getmAllMatieresNameForSpinner();
        if(!matieres.isEmpty()){
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, matieres);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerMatiere.setAdapter(dataAdapter);
        }else{

        }

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scan_cd){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new BookLoader(this, queryString);
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
            String scanContent = scanningResult.getContents();
            mEditISBNView.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void searchBooks(View view) {
        String queryString = mEditISBNView.getText().toString();
        mEditCodeBarreView.setText(queryString);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && queryString.length() != 0) {
            new FetchBook(mEditCodeBarreView, mEditTitleView, mEditEditeurView, mEditDescriptionView).execute(queryString);
            Toast load = Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT);
            load.show();
        } else {
            if (queryString.length() == 0) {
                Toast term = Toast.makeText(getApplicationContext(), "NO SEARCH TERM !", Toast.LENGTH_SHORT);
                term.show();
            } else {
                Toast net = Toast.makeText(getApplicationContext(), "NO NETWORK !", Toast.LENGTH_SHORT);
                net.show();
            }
        }
    }

    public void addBook(View view){
        newBook = new Livre();
        newBook.setTitle(mEditTitleView.getText().toString());
        newBook.setCode_barre(mEditCodeBarreView.getText().toString());
        newBook.setMatiere(mSpinnerMatiere.getSelectedItem().toString().toUpperCase());
        newBook.setEditeur(mEditEditeurView.getText().toString());
        newBook.setDescription(mEditDescriptionView.getText().toString());
        newBook.setAnnee(mSpinnerAnnee.getSelectedItem().toString().toUpperCase());
        newBook.setEtats(mSpinnerEtat.getSelectedItem().toString());
        newBook.setCommenataires(mEditCommentaire.getText().toString());
        newBook.setStatuts(StatutsLivre.APREPARER.getType());
        mBookViewModel.insert(newBook);
        finish();
    }
}
