package com.bugra.familybudget.http;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.component.ProgressDialogHandler;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.http.constant.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class RetrieveMonthSummaryTask extends AsyncTask<String, Void, String> {

    private MainActivity activity;
    private int year;
    private int month;
    private ProgressDialog progressDialog;

    public RetrieveMonthSummaryTask(MainActivity activity, int year, int month) {
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
    protected String doInBackground(String... params) {
        String url = Constants.getUrlForMonthSummary(year, month);
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
                String limitString = "";
                if(!summaryJson.getString("tagId").equals("null")) {
                    Tag tag = Tag.getTag(summaryJson.getInt("tagId"));
                    tagName = tag.getName();

                    if(tag.getLimit() != null) {
                        limitString = " (" + tag.getLimit().doubleValue() + " TL)";
                    }
                }
                BigDecimal amount = new BigDecimal(summaryJson.getDouble("totalAmount"));

                message += tagName + " = " + amount.doubleValue()*-1 + " TL" + limitString + "\n";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setTitle("Aylık Özet")
                    .setMessage(message)
                    .setPositiveButton("Tamam", null)
                    .create().show();

        } catch (JSONException e) {
            Toast.makeText(activity, "Error loading data.", Toast.LENGTH_LONG).show();
        }

    }

}
