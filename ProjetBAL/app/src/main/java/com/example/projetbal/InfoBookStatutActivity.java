package com.example.projetbal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.projetbal.object.Constantes;
import com.example.projetbal.object.FoundLivre;
import com.example.projetbal.object.book.Livre;

import androidx.appcompat.app.AppCompatActivity;

public class InfoBookStatutActivity extends AppCompatActivity {
    private FoundLivre livre;
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
        setContentView(R.layout.info_book);

        this.cdBEdit = findViewById(R.id.edit_cd);
        this.titleEdit = findViewById(R.id.edit_titre);
        this.matiereSpinner = findViewById(R.id.matiere);
        this.infoLivre = findViewById(R.id.edit_descrip);
        this.anneSpinner = findViewById(R.id.annee);
        this.editeurEdit = findViewById(R.id.editeur);
        this.etatsSpinner = findViewById(R.id.etatLivre);
        this.commEdit = findViewById(R.id.commentaire);
        this.statutSpinner = findViewById(R.id.statut);

        Intent intent = getIntent();
        livre = intent.getParcelableExtra("livre");
        current = livre.getLivre();

        String[] arrayMatiere = getResources().getStringArray(R.array.matiere);
        String[] arryAnne = getResources().getStringArray(R.array.annee);
        String[] arryEtat = getResources().getStringArray(R.array.etat_livre);
        String[] arryStatut = getResources().getStringArray(R.array.statut_livre);
        int idxMatiere = getIndex(arrayMatiere, current.getMatiere());
        int idxAnne = getIndex(arryAnne,current.getAnnee());
        int idxEtat = getIndex(arryEtat,current.getEtats());
        int idxStatut = getIndex(arryStatut,current.getStatuts());

        this.cdBEdit.setHint(current.getCode_barre());
        this.titleEdit.setHint(current.getTitle());
        this.matiereSpinner.setSelection(idxMatiere);
        this.infoLivre.setHint(current.getDescription());
        this.anneSpinner.setSelection(idxAnne);
        this.editeurEdit.setHint(current.getEditeur());
        this.etatsSpinner.setSelection(idxEtat);
        this.commEdit.setHint(current.getCommenataires());
        this.statutSpinner.setSelection(idxStatut);

        final String oldM = current.getMatiere();
        final String oldA = current.getAnnee();
        final String oldE = current.getEtats();
        final String oldS = current.getStatuts();

        final Button button = findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(cdBEdit.getText().toString())){
                    setResult(Constantes.INFO_BOOK_FAIL, replyIntent);
                } else {
                    if(!TextUtils.isEmpty(cdBEdit.getText())){
                        current.setCode_barre(cdBEdit.getText().toString());
                    }
                    if(!TextUtils.isEmpty(titleEdit.getText())){
                        current.setTitle(titleEdit.getText().toString());
                    }
                    if(!matiereSpinner.getSelectedItem().toString().equals(oldM)){
                        current.setMatiere(matiereSpinner.getSelectedItem().toString());
                    }
                    if(!TextUtils.isEmpty(infoLivre.getText())){
                        current.setDescription(infoLivre.getText().toString());
                    }
                    if(!anneSpinner.getSelectedItem().toString().equals(oldA)){
                        current.setAnnee(anneSpinner.getSelectedItem().toString());
                    }
                    if(!TextUtils.isEmpty(editeurEdit.getText())){
                        current.setEditeur(editeurEdit.getText().toString());
                    }
                    if(!etatsSpinner.getSelectedItem().toString().equals(oldE)){
                        current.setEtats(etatsSpinner.getSelectedItem().toString());
                    }
                    if(!TextUtils.isEmpty(commEdit.getText())){
                        current.setCommenataires(commEdit.getText().toString());
                    }
                    if(!statutSpinner.getSelectedItem().toString().equals(oldS)){
                        current.setStatuts(statutSpinner.getSelectedItem().toString());
                    }

                    livre.setLivre(current);
                    replyIntent.putExtra("livre",livre);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        final Button buttonR = findViewById(R.id.retour);
        buttonR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent reply = new Intent();
                setResult(Constantes.RETOUR_ACTIVITY_REQUEST_CODE,reply);
                finish();
            }
        });
    }

    public int getIndex(String[] tab, String spin){
        for(int i = 0; i < tab.length; i++){
            if(tab[i].equals(spin)){
                return i;
            }
        }
        return -1;
    }
}
