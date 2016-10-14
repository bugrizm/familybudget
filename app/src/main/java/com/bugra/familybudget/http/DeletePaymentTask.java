package com.bugra.familybudget.http;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.component.ProgressDialogHandler;
import com.bugra.familybudget.component.ResultHandler;
import com.bugra.familybudget.http.constant.Constants;

public class DeletePaymentTask extends AsyncTask<String, Void, Integer> {

    private MainActivity activity;
    private Integer paymentId;
    protected ProgressDialog progressDialog;

    public DeletePaymentTask(MainActivity activity, Integer paymentId) {
        this.activity = activity;
        this.paymentId = paymentId;
        progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String url = Constants.getUrlForDeletePayment(paymentId);
        return HttpRequestMaker.getInstance().doDelete(url);
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);
        new ResultHandler(activity, resultCode, new DialogOnClickListener()).handle();
    }

    class DialogOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DeletePaymentTask.this.activity.retrievePaymentList();
        }
    }

}
