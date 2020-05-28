package com.example.projetbal;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.projetbal.object.book.Livre;

import androidx.appcompat.app.AppCompatActivity;

public class InfoBookStatutActivity extends AppCompatActivity {
    private Livre current;

    private EditText cdBEdit;
    private EditText titleEdit;
    private Spinner matiereSpinner;
    private EditText infoLivre;
    private Spinner anneSpinner;
    private EditText editeurEdit;
    private Spinner etatsSpinner;
    private EditText commEdit;
    private Spinner statutSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
