package com.example.projetlivre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projetlivre.POST.SendLivres;
import com.example.projetlivre.dataB.BookViewModel;
import com.example.projetlivre.listadapter.NewBookListAdapter;
import com.example.projetlivre.object.Constantes;
import com.example.projetlivre.object.Livre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListeNewBookActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newliste_livres);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NewBookListAdapter adapter = new NewBookListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        FloatingActionButton retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ListeNewBookActivity.this, NewBookActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast up = Toast.makeText(getApplicationContext(), "UPLOAD !", Toast.LENGTH_SHORT);
                up.show();
                List<Livre> livres = mBookViewModel.getmAllBooks().getValue();
                String body = "";
                for(Livre l : livres){
                    body += l.toString()+"\n";
                }
                System.out.println(body);
                JSONObject postData = new JSONObject();
                try {
                    postData.put("user","test");
                    postData.put("methode","Nouveau(x) livre(s)");
                    postData.put("livres",livres);
                    new SendLivres().execute("http://51.178.42.152:8080/",postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mBookViewModel.getmAllBooks().observe(this, new Observer<List<Livre>>() {
            @Override
            public void onChanged(@Nullable final List<Livre> book) {
                // Update the cached copy of the words in the adapter.
                adapter.setBooks(book);
            }
        });
    }

    public void removeBook(Livre current){
        mBookViewModel.delete(current);
    }

    public void infosBook(Livre current){
        Intent intent = new Intent( ListeNewBookActivity.this, InfoBookActivity.class);
        intent.putExtra("livre",current);
        startActivityForResult(intent, Constantes.INFO_BOOK_ACTIVITY);
    }
}
