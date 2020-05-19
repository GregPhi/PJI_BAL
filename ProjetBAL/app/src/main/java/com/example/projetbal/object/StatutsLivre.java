package com.example.projetbal.object;

import android.os.Parcel;
import android.os.Parcelable;

public enum StatutsLivre implements Parcelable {
    // ATTRIBUTS
    /**/
    APREPARER("A préparer"),PREPARE("Préparé"),EMPRUNTE("Emprunté"),RENDU("Rendu"),AUTRE("Autre");
    /**/
    private String type;

    // CONSTRUCTOR

    /**
     *
     * @param t
     */
    StatutsLivre(String t){
        this.type = t;
    }

    // GETTER

    /**
     *
     * @return
     */
    public String getType(){
        return this.type;
    }

    public void setType(String t){ this.type = t;}

    // PARCELABLE
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StatutsLivre createFromParcel(Parcel in) {
            return StatutsLivre.values()[in.readInt()];
        }

        public StatutsLivre[] newArray(int size) {
            return new StatutsLivre[size];
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
