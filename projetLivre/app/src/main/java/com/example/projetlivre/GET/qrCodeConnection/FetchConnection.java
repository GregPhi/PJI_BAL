package com.example.projetlivre.GET.qrCodeConnection;

import android.os.AsyncTask;

import com.example.projetlivre.object.Livre;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchConnection extends AsyncTask<String, Void, String> {
    private String user;
    private String methode;
    private List<Livre> livres;

    public FetchConnection() {
        this.user = "";
        this.methode = "";
        this.livres = new ArrayList<Livre>();
    }

    public String getUser(){ return this.user; }
    public String getMethode(){ return this.methode; }
    public List<Livre> getLivres(){ return this.livres; }

    @Override
    protected String doInBackground(String... strings) {
        return QRCodeNetworkUtils.getCoonectionInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            String u = null;
            String m = null;
            u = jsonObject.getString("user");
            m = jsonObject.getString("methode");
            if (u != null ) {
                this.user = u;
            } else {
                this.user = "NO DATA";
            }
            if (m != null) {
                this.methode = m;
            } else {
                this.methode = "NO DATA";
            }
            if(this.methode == "Rendu de livre(s)" || this.methode == "Prêt de livre(s)"){
                JSONArray livreArray = jsonObject.getJSONArray("livres");
                int i = 0;
                while(i < livreArray.length()){
                    JSONObject livre = livreArray.getJSONObject(i);
                    Livre l = new Livre();
                    if(livre.has("code_barre")){
                        l.setCode_barre(livre.getString("code_barre"));
                    }
                    if(livre.has("title")){
                        l.setTitle(livre.getString("title"));
                    }
                    if(livre.has("matiere")){
                        l.setMatiere(livre.getString("matiere"));
                    }
                    if(livre.has("statut")){
                        l.setStatuts(livre.getString("statut"));
                    }
                    if(livre.has("etat")){
                        l.setEtats(livre.getString("etat"));
                    }
                    if(livre.has("commentaire")){
                        l.setCommenataires(livre.getString("commentaire"));
                    }
                    if(livre.has("annee")){
                        l.setAnnee(livre.getString("annee"));
                    }
                    this.livres.add(l);
                    i++;
                }
            }
        } catch (Exception e) {
        }
    }
}

