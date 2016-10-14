package com.bugra.familybudget.component;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProgressDialogHandler {

    private static final String LOADING_TEXT = "Yükleniyor...";

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(LOADING_TEXT);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void showProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void closeProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
