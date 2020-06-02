package com.example.projetbal.object.book;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "matiere_table")
public class Matiere {
    // ATTRIBUST
    /**/
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    /**/
    @NonNull
    private String nom;

    public Matiere(){

    }
    public Matiere(String n){
        this.nom = n;
    }

    public int getId(){ return this.id; }
    public void setId(int i){ this.id = i;}

    public String getNom(){ return this.nom; }
    public void setNom(String n){ this.nom = n; }
}
