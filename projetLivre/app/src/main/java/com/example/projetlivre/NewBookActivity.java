package com.example.projetlivre;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projetlivre.GET.book.BookLoader;
import com.example.projetlivre.GET.book.FetchBook;
import com.example.projetlivre.dataB.BookViewModel;

import com.example.projetlivre.object.Livre;
import com.example.projetlivre.object.StatutsLivre;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class NewBookActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<String> {
    public static BookViewModel mBookViewModel;
    private Livre newBook = null;

    private EditText mEditISBNView;

    private EditText mEditTitleView;
    private EditText mEditCodeBarreView;
    private EditText mEditMatiereView;
    private EditText mEditEditeurView;
    private EditText mEditDescriptionView;
    private Spinner mSpinnerAnnee;
    private Spinner mSpinnerEtat;
    private EditText mEditCommentaire;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_book);

        mEditISBNView = findViewById(R.id.isbnScan);

        mEditCodeBarreView = findViewById(R.id.edit_cd);
        mEditTitleView = findViewById(R.id.edit_titre);
        mEditMatiereView = findViewById(R.id.edit_matiere);
        mEditEditeurView = findViewById(R.id.editeur);
        mEditDescriptionView = findViewById(R.id.edit_descrip);
        mSpinnerAnnee = findViewById(R.id.annee);
        mSpinnerEtat = findViewById(R.id.etatLivre);
        mEditCommentaire = findViewById(R.id.commentaire);

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

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

    public void displayListeNewBooks(View view){
        Intent intent = new Intent(NewBookActivity.this, ListeNewBookActivity.class);
        startActivity(intent);
    }

    public void addBook(View view){
        newBook = new Livre();
        newBook.setTitle(mEditTitleView.getText().toString());
        newBook.setCode_barre(mEditCodeBarreView.getText().toString());
        newBook.setMatiere(mEditMatiereView.getText().toString());
        newBook.setEditeur(mEditEditeurView.getText().toString());
        newBook.setDescription(mEditDescriptionView.getText().toString());
        newBook.setAnnee(mSpinnerAnnee.getSelectedItem().toString());
        newBook.setEtats(mSpinnerEtat.getSelectedItem().toString());
        newBook.setCommenataires(mEditCommentaire.getText().toString());
        newBook.setEtats(StatutsLivre.APREPARER.getType());
        mBookViewModel.insert(newBook);
    }
}
