package com.example.projetbal.object;

public class FoundLivre {
    private Livre livre;
    private boolean found;
    public FoundLivre(){
        this.livre = new Livre();
        this.found = false;
    }
    public FoundLivre(Livre livre){
        this.livre = livre;
        this.found = false;
    }
    public Livre getLivre(){
        return this.livre;
    }
    public boolean getFound(){
        return this.found;
    }
    public void setLivre(Livre livre){
        this.livre = livre;
    }
    public void setFound(boolean bool){
        this.found = bool;
    }
}
