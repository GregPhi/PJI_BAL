package com.example.projetlivre;

import android.content.Intent;
import android.os.Bundle;

import com.example.projetlivre.object.Livre;

import androidx.appcompat.app.AppCompatActivity;

public class InfoBookActivity extends AppCompatActivity {
    private Livre current;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_book);
        Intent intent = getIntent();
        current = intent.getParcelableExtra("livre");
    }
}
