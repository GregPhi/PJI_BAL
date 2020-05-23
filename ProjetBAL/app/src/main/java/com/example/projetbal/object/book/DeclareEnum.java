package com.example.projetbal.object.book;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class Test {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TERMINALE, PREMIERE, SECONDE})
    public @interface Niveau {}
    public static final String TERMINALE = "TERMINALE";
    public static final String PREMIERE = "PREMIERE";
    public static final String SECONDE = "SECONDE";
    //TERMINALE, PREMIERE, SECONDE
}
