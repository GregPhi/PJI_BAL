package com.example.projetlivretestrequest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // https://www.youtube.com/watch?v=yhv8l9F44qo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void strRequest(View view){
        Intent intent = new Intent(MainActivity.this,StrRequestActivity.class);
        startActivity(intent);
    }

    public void jsonRequest(View view){
        Intent intent = new Intent(MainActivity.this,JsonRequestActivity.class);
        startActivity(intent);
    }
}
