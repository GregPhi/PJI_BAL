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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.lifecycle.ViewModelProviders;

public class NewBookActivity extends AppCompatActivity {

    public static BookViewModel mBookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        RecyclerView recyclerView = findViewById(R.id.newBook);
        final NewBookListAdapter adapter = new NewBookListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        FloatingActionButton retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( NewBookActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton upload = findViewById(R.id.uploadNewBook);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == Constantes.INFO_BOOK_OK){
            Livre current = intent.getParcelableExtra("livre");
            mBookViewModel.updateBook(current);
        }
        if(resultCode == Constantes.INFO_BOOK_FAIL){
            Toast.makeText(getApplicationContext(), "Erreyr lors de la modification", Toast.LENGTH_LONG).show();
        }
    }

    public void removeBook(Livre current){
        mBookViewModel.delete(current);
    }

    public void infosBook(Livre current){
        Intent intent = new Intent(NewBookActivity.this, InfoBookActivity.class);
        intent.putExtra("livre",current);
        startActivityForResult(intent, Constantes.INFO_BOOK_ACTIVITY);
    }
}
