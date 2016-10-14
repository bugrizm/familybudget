package com.bugra.familybudget.http;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.http.constant.Constants;
import com.bugra.familybudget.component.ProgressDialogHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class RetrieveYearSummaryTask extends AsyncTask<String, Void, String> {

    private MainActivity activity;
    private int year;
    private ProgressDialog progressDialog;

    public RetrieveYearSummaryTask(MainActivity activity, int year) {
        this.activity = activity;
        this.year = year;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = Constants.getUrlForYearSummary(year);
        return HttpRequestMaker.getInstance().doGet(url);
    }

    @Override
    protected void onPostExecute(String result) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);

        try {
            JSONArray jsonArray = new JSONArray(result);

            String message = "";

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject summaryJson = jsonArray.getJSONObject(i);
                String tagName = "Hepsi";
                if(! summaryJson.getString("tagId").equals("null")) {
                    tagName = Tag.getTag(summaryJson.getInt("tagId")).getName();
                }
                BigDecimal amount = new BigDecimal(summaryJson.getDouble("totalAmount"));

                message += tagName + " = " + amount.doubleValue()*-1 + " TL\n";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setTitle("Yıllık Özet")
                    .setMessage(message)
                    .setPositiveButton("Tamam", null)
                    .create().show();

        } catch (JSONException e) {
            Toast.makeText(activity, "Error loading data.", Toast.LENGTH_LONG).show();
        }

    }

}
