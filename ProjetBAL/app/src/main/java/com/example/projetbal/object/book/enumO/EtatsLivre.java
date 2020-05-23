package com.example.projetbal.object.book;

import android.os.Parcel;
import android.os.Parcelable;

public enum EtatsLivre implements Parcelable {
    // ATTRIBUTS
    /**/
    NEUF("Neuf"),BONETAT("Bon état"),USUREFAIBLE("Usure faible"),USUREMOYENNE("Usure moyenne"),USUREFORTE("Usure forte"),AUTRE("Autre");
    /**/
    private String type;

    // CONSTRUCTOR

    /**
     *
     * @param t
     */
    EtatsLivre(String t){
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
        public EtatsLivre createFromParcel(Parcel in) {
            return EtatsLivre.values()[in.readInt()];
        }

        public EtatsLivre[] newArray(int size) {
            return new EtatsLivre[size];
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
