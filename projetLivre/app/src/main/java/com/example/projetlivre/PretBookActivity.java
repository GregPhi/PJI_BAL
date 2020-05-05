package com.example.projetlivre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.projetlivre.POST.SendLivres;
import com.example.projetlivre.listadapter.PretBookListAdapter;
import com.example.projetlivre.object.Livre;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PretBookActivity extends AppCompatActivity {
    private List<Livre> pretLivres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pret);
        Intent intent = getIntent();
        pretLivres = intent.getParcelableArrayListExtra("livres");

        RecyclerView recyclerView = findViewById(R.id.recyclerviewPret);
        final PretBookListAdapter adapter = new PretBookListAdapter(this);
        adapter.setBooks(pretLivres);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( PretBookActivity.this, PremierePageActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast up = Toast.makeText(getApplicationContext(), "UPLOAD !", Toast.LENGTH_SHORT);
                up.show();
                JSONObject postData = new JSONObject();
                try {
                    postData.put("user","test");
                    postData.put("methode","Prêt de livre(s)");
                    postData.put("livres",pretLivres);
                    new SendLivres().execute("http://51.178.42.152:8080/",postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void infoBook(Livre current){

    }
}
