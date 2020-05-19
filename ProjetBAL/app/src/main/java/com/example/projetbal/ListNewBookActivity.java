package com.example.projetbal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projetbal.dataB.BookViewModel;
import com.example.projetbal.listadapter.NewBookListAdapter;
import com.example.projetbal.object.Constantes;
import com.example.projetbal.object.Livre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListNewBookActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        RecyclerView recyclerView = findViewById(R.id.newBook);
        final NewBookListAdapter adapter = new NewBookListAdapter(ListNewBookActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        FloatingActionButton scanNewBook = findViewById(R.id.scanNewBook);
        scanNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListNewBookActivity.this,NewBookActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton uploadNewBook = findViewById(R.id.uploadNewBook);
        uploadNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == Constantes.INFO_BOOK_OK){
            Livre book = intent.getParcelableExtra("livre");
            mBookViewModel.updateBook(book);
        }
        if(requestCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(), "Merci d'indiquer un code barre", Toast.LENGTH_LONG).show();
        }
    }

    public void removeBook(Livre current){
        mBookViewModel.delete(current);
    }

    public void infosBook(Livre current){
        Intent intent = new Intent( ListNewBookActivity.this, InfoBookActivity.class);
        intent.putExtra("livre",current);
        startActivityForResult(intent, Constantes.INFO_BOOK_ACTIVITY);
    }
}
