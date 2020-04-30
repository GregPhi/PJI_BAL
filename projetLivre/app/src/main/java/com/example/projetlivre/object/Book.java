package com.example.projetlivre.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book implements Parcelable {
    // ATTRIBUST
    /**/
    @PrimaryKey
    @NonNull
    private String id;
    /**/
    @NonNull
    private String code_barre;
    /**/
    @NonNull
    private String title;
    /**/
    private String matiere;
    private String editeur;
    /**/
    private String description;
    /**/
    private String commenataires;
    /**/
    private String annee;
    /**/
    private String classe;
    /**/
    private String etats;

    // CONSTRUCTOR
    public Book(){
        this.id = "";
        this.code_barre = "";
        this.title = "";
        this.matiere = "";
        this.editeur = "";
        this.commenataires = "";
        this.description = "";
        this.annee = "";
        this.classe = "";
        this.etats = "";
    }

    // GETTER AND SETTER

    public void setId(String i){
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

    public String getId(){
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

    public String getDescription(){ return this.description; }

    public String getEditeur(){ return this.editeur; }

    // PARCELABLE
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(Parcel in) {
        this.id = in.readString();
        this.code_barre = in.readString();
        this.title = in.readString();
        this.matiere = in.readString();
        this.editeur = in.readString();
        this.commenataires = in.readString();
        this.annee = in.readString();
        this.classe = in.readString();
        this.etats = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.code_barre);
        dest.writeString(this.title);
        dest.writeString(this.matiere);
        dest.writeString(this.editeur);
        dest.writeString(this.commenataires);
        dest.writeString(this.annee);
        dest.writeString(this.classe);
        dest.writeString(this.etats);
    }
}
