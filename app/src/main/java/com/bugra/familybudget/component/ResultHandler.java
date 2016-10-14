package com.bugra.familybudget.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ResultHandler {

    private Activity activity;
    private int resultCode;
    private DialogInterface.OnClickListener resultOnClickListener;

    public ResultHandler(Activity activity, int resultCode, DialogInterface.OnClickListener resultOnClickListener) {
        this.activity = activity;
        this.resultCode = resultCode;
        this.resultOnClickListener = resultOnClickListener;
    }

    public void handle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if(resultCode == 200) {
            builder.setTitle("BİLGİ")
                    .setMessage("İşlem başarıyla tamamlandı.")
                    .setPositiveButton("Tamam", resultOnClickListener)
                    .create().show();
        }
        else {
            builder.setTitle("HATA")
                    .setMessage("İşlem tamamlanamadı.")
                    .setPositiveButton("Tamam", resultOnClickListener)
                    .create().show();
        }
    }

}
