package com.example.projetlivre.object;

import android.os.Parcel;
import android.os.Parcelable;

public enum Annee implements Parcelable {
    // ATTRIBUTS
    /**/
    SECONDE("Seconde"),PREMIERE("Premiere"),TERMINALE("Terminale");
    /**/
    private String annee;

    // CONSTRUCTOR

    /**
     *
     * @param a
     */
    Annee(String a){
        this.annee = a;
    }

    // GETTER

    /**
     *
     * @return
     */
    public String getAnnee(){
        return this.annee;
    }

    // PARCELABLE
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Annee createFromParcel(Parcel in) {
            return Annee.values()[in.readInt()];
        }

        public Annee[] newArray(int size) {
            return new Annee[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }
}
