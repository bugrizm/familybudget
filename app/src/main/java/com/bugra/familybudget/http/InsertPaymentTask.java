package com.bugra.familybudget.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.bugra.familybudget.NewPaymentActivity;
import com.bugra.familybudget.component.ProgressDialogHandler;
import com.bugra.familybudget.component.ResultHandler;
import com.bugra.familybudget.entity.PaymentDTO;
import com.bugra.familybudget.http.constant.Constants;

public class InsertPaymentTask extends AsyncTask<String, Void, Integer> {

    private NewPaymentActivity activity;
    private PaymentDTO paymentDTO;
    private ProgressDialog progressDialog;

    public InsertPaymentTask(NewPaymentActivity activity, PaymentDTO paymentDTO) {
        this.activity = activity;
        this.paymentDTO = paymentDTO;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String url = Constants.getUrlForInsertPayment();
        return HttpRequestMaker.getInstance().doPost(url, paymentDTO.toJsonString());
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);
        new ResultHandler(activity, resultCode, new DialogOnClickListener()).handle();
    }

    class DialogOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            InsertPaymentTask.this.activity.setResult(Activity.RESULT_OK);
            InsertPaymentTask.this.activity.finish();
        }
    }

}
