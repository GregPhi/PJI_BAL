package com.example.projetlivre.GET.qrCodeConnection;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class QRCodeLoader extends AsyncTaskLoader<String> {

    private String mQueryString;

    public QRCodeLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return QRCodeNetworkUtils.getCoonectionInfo(mQueryString);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }
}
