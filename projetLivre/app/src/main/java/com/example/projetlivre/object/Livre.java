package com.example.projetlivre.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Livre implements Parcelable {
    // ATTRIBUST
    /**/
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    /**/
    @NonNull
    private String code_barre;
    /**/
    @NonNull
    private String title;
    /**/
    private String matiere;
    /**/
    private String editeur;
    /**/
    private String description;
    /**/
    private String etats;
    /**/
    private String commenataires;
    /**/
    private String annee;
    /**/
    private String classe;
    /**/
    private String statuts;

    // CONSTRUCTOR
    public Livre(){
        this.code_barre = "";
        this.title = "";
        this.matiere = "";
        this.editeur = "";
        this.commenataires = "";
        this.description = "";
        this.annee = "";
        this.classe = "";
        this.etats = "";
        this.statuts = "";
    }

    // PARCEL
    protected Livre(Parcel in) {
        id = in.readInt();
        code_barre = in.readString();
        title = in.readString();
        matiere = in.readString();
        editeur = in.readString();
        description = in.readString();
        etats = in.readString();
        commenataires = in.readString();
        annee = in.readString();
        classe = in.readString();
        statuts = in.readString();
    }

    public static final Creator<Livre> CREATOR = new Creator<Livre>() {
        @Override
        public Livre createFromParcel(Parcel in) {
            return new Livre(in);
        }

        @Override
        public Livre[] newArray(int size) {
            return new Livre[size];
        }
    };

    // GETTER AND SETTER
    public void setId(int i){
        this.id = i;
    }

    public void setCode_barre(String c){
        this.code_barre = c;
    }

    public void setTitle(String t){
        this.title = t;
    }

    public void setMatiere(String m){
        this.matiere = m;
    }

    public void setCommenataires(String d){
        this.commenataires = d;
    }

    public void setAnnee(String a){
        this.annee = a;
    }

    public void setClasse(String c){
        this.classe = c;
    }

    public void setEtats(String e){
        this.etats = e;
    }

    public void setDescription(String d){ this.description = d; }

    public void setEditeur(String e){ this.editeur = e; }

    public void setStatuts(String s){ this.statuts = s; }

    public int getId(){
        return this.id;
    }

    public String getCode_barre(){
        return this.code_barre;
    }

    public String getTitle(){
        return this.title;
    }

    public String getMatiere(){
        return this.matiere;
    }

    public String getCommenataires(){
        return this.commenataires;
    }

    public String getAnnee(){
        return this.annee;
    }

    public String getClasse(){
        return this.classe;
    }

    public String getEtats(){
        return this.etats;
    }

    public String getStatuts(){
        return this.statuts;
    }

    public String getDescription(){ return this.description; }

    public String getEditeur(){ return this.editeur; }

    public String toString(){
        return "Code Barres :"+this.code_barre+" Titre : "+this.title+" Matiere : "+this.matiere;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(code_barre);
        dest.writeString(title);
        dest.writeString(matiere);
        dest.writeString(editeur);
        dest.writeString(description);
        dest.writeString(etats);
        dest.writeString(commenataires);
        dest.writeString(annee);
        dest.writeString(classe);
        dest.writeString(statuts);
    }
}
