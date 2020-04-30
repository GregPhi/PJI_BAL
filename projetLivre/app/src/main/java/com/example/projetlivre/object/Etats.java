package com.example.projetlivre.object;

import android.os.Parcel;
import android.os.Parcelable;

public enum Etats implements Parcelable{
    // ATTRIBUTS
    /**/
    TOPREPARE("A PREPARER"),READY("A PRETER"),LOANED("PRETE"),RETRUNED("RENDU");
    /**/
    private String type;

    // CONSTRUCTOR

    /**
     *
     * @param t
     */
    Etats(String t){
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

    // PARCELABLE
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Etats createFromParcel(Parcel in) {
            return Etats.values()[in.readInt()];
        }

        public Etats[] newArray(int size) {
            return new Etats[size];
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
