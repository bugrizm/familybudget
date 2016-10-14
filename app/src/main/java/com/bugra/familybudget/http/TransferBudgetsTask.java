package com.bugra.familybudget.http;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.component.ResultHandler;
import com.bugra.familybudget.http.constant.Constants;
import com.bugra.familybudget.component.ProgressDialogHandler;

public class TransferBudgetsTask extends AsyncTask<String, Void, Integer> {
    private MainActivity activity;

    private ProgressDialog progressDialog;

    private int year;
    private int month;

    public TransferBudgetsTask(MainActivity activity, int year, int month) {
        this.activity = activity;
        this.year = year;
        this.month = month;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String url = Constants.getUrlForTransferBudget(year, month);
        return HttpRequestMaker.getInstance().doPost(url, null);
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);
        new ResultHandler(activity, resultCode, new DialogOnClickListener()).handle();
    }

    class DialogOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            TransferBudgetsTask.this.activity.retrievePaymentList();
        }
    }

}
