package com.example.projetlivretestrequest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class StrRequestActivity extends AppCompatActivity {
    private EditText editStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.string_request);
        editStr = findViewById(R.id.editStr);
    }

    public void sendRequest(View view){
        if(!TextUtils.isEmpty(editStr.getText().toString())){

        }
    }
}
