package com.example.projetbal.object;

import com.example.projetbal.object.book.Livre;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foundbook_table")
public class FoundLivre {
    @PrimaryKey(autoGenerate = true)
    private int id_found;
    @Embedded private Livre livre;
    private boolean found;

    public FoundLivre(){
        this.livre = null;
        this.found = false;
    }
    public FoundLivre(Livre code_barre){
        this.livre = code_barre;
        this.found = false;
    }

    public int getId_found(){ return this.id_found; }
    public Livre getLivre(){
        return this.livre;
    }
    public boolean getFound(){
        return this.found;
    }
    public void setId_found(int id){ this.id_found = id; }
    public void setLivre(Livre livre){
        this.livre = livre;
    }
    public void setFound(boolean bool){
        this.found = bool;
    }
}
