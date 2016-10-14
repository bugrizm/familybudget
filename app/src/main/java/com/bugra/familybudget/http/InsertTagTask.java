package com.bugra.familybudget.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.bugra.familybudget.NewTagActivity;
import com.bugra.familybudget.component.ResultHandler;
import com.bugra.familybudget.entity.TagDTO;
import com.bugra.familybudget.http.constant.Constants;
import com.bugra.familybudget.component.ProgressDialogHandler;

public class InsertTagTask extends AsyncTask<String, Void, Integer> {

    private NewTagActivity activity;
    private TagDTO tagDTO;
    private ProgressDialog progressDialog;

    public InsertTagTask(NewTagActivity activity, TagDTO tagDTO) {
        this.activity = activity;
        this.tagDTO = tagDTO;
        this.progressDialog = ProgressDialogHandler.createProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        ProgressDialogHandler.showProgressDialog(progressDialog);
    }

    @Override
    protected Integer doInBackground(String... params) {
        String url = Constants.getUrlForInsertTag();
        return HttpRequestMaker.getInstance().doPost(url, tagDTO.toJsonString());
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        ProgressDialogHandler.closeProgressDialog(progressDialog);
        new ResultHandler(activity, resultCode, new DialogOnClickListener()).handle();
    }

    class DialogOnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            InsertTagTask.this.activity.setResult(Activity.RESULT_OK);
            InsertTagTask.this.activity.finish();
        }
    }

}
