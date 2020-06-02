package com.example.projetbal.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.projetbal.object.book.Livre;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foundbook_table")
public class FoundLivre implements Parcelable {
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

    protected FoundLivre(Parcel in) {
        id_found = in.readInt();
        livre = in.readParcelable(Livre.class.getClassLoader());
        found = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_found);
        dest.writeParcelable(livre, flags);
        dest.writeByte((byte) (found ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoundLivre> CREATOR = new Creator<FoundLivre>() {
        @Override
        public FoundLivre createFromParcel(Parcel in) {
            return new FoundLivre(in);
        }

        @Override
        public FoundLivre[] newArray(int size) {
            return new FoundLivre[size];
        }
    };

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
