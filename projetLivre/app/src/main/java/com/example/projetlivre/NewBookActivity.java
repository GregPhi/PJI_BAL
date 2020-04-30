package com.example.projetlivre;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetlivre.GET.book.BookLoader;
import com.example.projetlivre.GET.book.BookNetworkUtils;
import com.example.projetlivre.object.Book;
import com.example.projetlivre.dataB.BookViewModel;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class NewBookActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<String> {
    public static BookViewModel mBookViewModel;
    private Book newBook = null;

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

    public class FetchBook extends AsyncTask<String, Void, String> {
        private WeakReference<TextView> mCDText;
        private WeakReference<TextView> mTitleText;
        private WeakReference<TextView> mPubliText;
        private WeakReference<TextView> mDescriptText;

        FetchBook(TextView cdText, TextView titleText, TextView publiText, TextView descriptText) {
            this.mCDText = new WeakReference<>(cdText);
            this.mTitleText = new WeakReference<>(titleText);
            this.mPubliText = new WeakReference<>(publiText);
            this.mDescriptText = new WeakReference<>(descriptText);
        }

        @Override
        protected String doInBackground(String... strings) {
            this.mCDText.get().setText(strings[0]);
            return BookNetworkUtils.getBookInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s);
                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // Initialize iterator and results fields.
                int i = 0;
                String title = null;
                String edit = null;
                String descipt = null;

                // Look for results in the items array, exiting
                // when both the title and author
                // are found or when all items have been checked.
                while (i < itemsArray.length() && (descipt == null && edit == null && title == null)) {
                    // Get the current item information.
                    JSONObject book = ((JSONArray) itemsArray).getJSONObject(i);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    // Try to get the author and title from the current item,
                    // catch if either field is empty and move on.
                    try {
                        title = volumeInfo.getString("title");
                        edit = volumeInfo.getString("publisher");
                        descipt = volumeInfo.getString("description");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Move to the next item.
                    i++;
                }

                // If both are found, display the result.
                if (title != null && edit != null && descipt != null) {
                    mTitleText.get().setText(title);
                    mPubliText.get().setText(edit);
                    mDescriptText.get().setText(descipt);
                } else {
                    // If none are found, update the UI to
                    // show failed results.
                    mTitleText.get().setText("NO RESULTS");
                    mPubliText.get().setText("NO RESULTS");
                    mDescriptText.get().setText("NO RESULTS");
                }

            } catch (Exception e) {
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                mTitleText.get().setText("NO RESULTS");
                mPubliText.get().setText("NO RESULTS");
                mDescriptText.get().setText("NO RESULTS");
            }
        }
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
        newBook = new Book();
        newBook.setTitle(mEditTitleView.getText().toString());
        newBook.setCode_barre(mEditCodeBarreView.getText().toString());
        newBook.setMatiere(mEditMatiereView.getText().toString());
        newBook.setEditeur(mEditEditeurView.getText().toString());
        newBook.setDescription(mEditDescriptionView.getText().toString());
        newBook.setAnnee(mSpinnerAnnee.getSelectedItem().toString());
        newBook.setEtats(mSpinnerEtat.getSelectedItem().toString());
        newBook.setCommenataires(mEditCommentaire.getText().toString());
        mBookViewModel.insert(newBook);
    }
}
