package com.bugra.familybudget.http;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.entity.Payment;
import com.bugra.familybudget.http.constant.Constants;
import com.bugra.familybudget.component.ProgressDialogHandler;

public class RetrievePaymentsTask extends AsyncTask<String, Void, String> {
    private MainActivity activity;
    private int year;
    private int month;
    private ProgressDialog progressDialog;
    private Integer tagId;

    public RetrievePaymentsTask(MainActivity activity, int year, int month, Integer tagId) {
        this.activity = activity;
        this.year = year;
        this.month = month;
        this.tagId = tagId;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... params) {
        String url = tagId == null ? Constants.getUrlForRetrievePayment(year, month) : Constants.getUrlForRetrievePayment(year, month, tagId);
        return HttpRequestMaker.getInstance().doGet(url);
    }

    @Override
    protected void onPostExecute(String result) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);

        try {
            List<Payment> paymentList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject paymentJson = jsonArray.getJSONObject(i);
                paymentList.add(Payment.parseJSON(paymentJson));
            }

            activity.initPaymentList(paymentList);

        } catch (JSONException e) {
            Toast.makeText(activity, "Error loading data.", Toast.LENGTH_LONG).show();
        }

    }

}
