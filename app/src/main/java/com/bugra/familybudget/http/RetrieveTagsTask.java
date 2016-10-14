package com.bugra.familybudget.http;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bugra.familybudget.MainActivity;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.http.constant.Constants;
import com.bugra.familybudget.component.ProgressDialogHandler;

public class RetrieveTagsTask extends AsyncTask<String, Void, String> {

    private MainActivity activity;
    private ProgressDialog progressDialog;

    private int retryCount;

    public RetrieveTagsTask(MainActivity activity, int retryCount) {
        this.activity = activity;
        this.retryCount = retryCount;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected String doInBackground(String... strings) {
        int numberOfConnectionAttempts = 0;

        String url = Constants.getUrlForRetrieveTags();
        String result = null;
        while(numberOfConnectionAttempts < Constants.MAX_NUMBER_OF_CONNECTION_ATTEMPTS && result == null) {
            try {
                result = HttpRequestMaker.getInstance().doGet(url);
                break;
            } catch (Exception e) {
                //Do nothing...
            }

            numberOfConnectionAttempts ++;

            //Wait before try to reconnect to the server.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);

        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject tagJson = jsonArray.getJSONObject(i);
                Tag tag = Tag.parseJSON(tagJson);
                Tag.putTag(tag.getId(), tag);
            }

            activity.retrievePaymentList();

        } catch (JSONException e) {
            activity.serverError(retryCount);
        } catch (NullPointerException e) {
            activity.serverError(retryCount);
        }

    }

}
