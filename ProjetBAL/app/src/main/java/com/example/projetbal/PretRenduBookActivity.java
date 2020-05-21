package com.example.projetbal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projetbal.listadapter.PretRenduBookListAdapter;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.Livre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PretRenduBookActivity extends AppCompatActivity {

    private String user;
    private String methode;
    private List<Livre> livres;
    private List<FoundLivre> foundLivres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret_rendu_book);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        methode = intent.getStringExtra("methode");
        livres = intent.getParcelableArrayListExtra("livres");
        for(Livre l : livres){
            foundLivres.add(new FoundLivre(l));
        }

        RecyclerView recyclerView = findViewById(R.id.pret_renduBook);
        final PretRenduBookListAdapter adapter = new PretRenduBookListAdapter(PretRenduBookActivity.this);
        adapter.setBooks(foundLivres);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton scanQRPretRendu = findViewById(R.id.scanQRPretRendu);
        scanQRPretRendu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(PretRenduBookActivity.this);
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            for(FoundLivre l : foundLivres){
                Livre liv = l.getLivre();
                if(liv.getCode_barre().equals(scanContent)){
                    l.setFound(true);
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void infosBook(Livre current){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Informations");
        dialog.setMessage(current.toString());
        dialog.setCancelable(true);
        dialog.show();
    }
}
