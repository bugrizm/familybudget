package com.bugra.familybudget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bugra.familybudget.adapter.PaymentListAdapter;
import com.bugra.familybudget.entity.Payment;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.http.DeletePaymentTask;
import com.bugra.familybudget.http.RetrieveMonthSummaryTask;
import com.bugra.familybudget.http.RetrievePaymentsTask;
import com.bugra.familybudget.http.RetrieveTagsTask;
import com.bugra.familybudget.http.RetrieveYearSummaryTask;
import com.bugra.familybudget.http.TransferBudgetsTask;
import com.bugra.familybudget.utils.CalendarUtils;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    private int currentMonth;
    private int currentYear;
    private Integer selectedTagId = null;

    @BindView(R.id.monthButton) Button monthButton;
    @BindView(R.id.paymentListView) ListView paymentListView;

    @BindString(R.string.error) String strError;
    @BindString(R.string.cant_connect_to_server) String strCantConnectToServer;
    @BindString(R.string.ok) String strOk;
    @BindString(R.string.it_will_be_deleted) String strItWillBeDeleted;
    @BindString(R.string.are_you_sure_to_delete) String strAreYouSureToDelete;
    @BindString(R.string.yes) String strYes;
    @BindString(R.string.no) String strNo;
    @BindString(R.string.all) String strAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int[] yearMonth = CalendarUtils.getCurrentMonthYear();
        currentMonth = yearMonth[0];
        currentYear = yearMonth[1];

        retrieveTags();
    }

    public void retrieveTags() {
        RetrieveTagsTask retrieveTagsTask = new RetrieveTagsTask(this, 1);
        retrieveTagsTask.execute();
    }

    public void serverError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(strError)
                .setMessage(strCantConnectToServer)
                .setPositiveButton(strOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                })
                .create().show();
    }

    public void serverError(int retryCount) {
        if(retryCount > 5) {
            serverError();
        } else {
            new RetrieveTagsTask(this, retryCount + 1).execute();
        }
    }

    public void retrievePaymentList() {
        RetrievePaymentsTask retrievePaymentsTask = new RetrievePaymentsTask(this, currentYear, currentMonth-1, selectedTagId);
        retrievePaymentsTask.execute();

        updateMonthButton();
    }

    private void updateMonthButton() {
        String monthString = getResources().getStringArray(R.array.months_array)[currentMonth-1].toUpperCase();
        monthButton.setText(monthString + " " + currentYear);
    }

    public void initPaymentList(List<Payment> paymentList) {
        PaymentListAdapter adapter = PaymentListAdapter.newInstance(getLayoutInflater(), paymentList);
        paymentListView.setAdapter(adapter);

        paymentListView.invalidate();

        paymentListView.setOnItemLongClickListener(new PaymentItemOnLongClickListener());
        paymentListView.setOnItemClickListener(new PaymentItemOnClickListener());
    }

    public void previousMonthButtonOnClick(View view) {
        int[] previousMonthYear = CalendarUtils.getPreviousMonthYear(currentMonth, currentYear);
        currentMonth = previousMonthYear[0];
        currentYear = previousMonthYear[1];

        retrievePaymentList();
    }

    public void nextMonthButtonOnClick(View view) {
        int[] nextMonthYear = CalendarUtils.getNextMonthYear(currentMonth, currentYear);
        currentMonth = nextMonthYear[0];
        currentYear = nextMonthYear[1];

        retrievePaymentList();
    }

    public void newPaymentButtonOnClick(View view) {
        Intent newPaymentActivityIntent = new Intent(this, NewPaymentActivity.class);

        startActivityForResult(newPaymentActivityIntent, NewPaymentActivity.NEW_PAYMENT_ACTIVITY_RESULT);
    }

    public void monthButtonOnClick(View view) {
        RetrieveMonthSummaryTask retrieveMonthSummaryTask = new RetrieveMonthSummaryTask(this, currentYear, currentMonth-1);
        retrieveMonthSummaryTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NewPaymentActivity.NEW_PAYMENT_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
                retrievePaymentList();
            }
        } else if (requestCode == NewTagActivity.NEW_TAG_ACTIVITY_RESULT) {
            if (resultCode == RESULT_OK) {
                retrieveTags();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_tag) {
            Intent newTagActivityIntent = new Intent(this, NewTagActivity.class);
            startActivityForResult(newTagActivityIntent, NewTagActivity.NEW_TAG_ACTIVITY_RESULT);
        } else if(id == R.id.action_year_summary) {
            RetrieveYearSummaryTask retrieveYearSummaryTask = new RetrieveYearSummaryTask(this, currentYear);
            retrieveYearSummaryTask.execute();
        } else if(id == R.id.action_transfer_budgets) {
            TransferBudgetsTask transferBudgetsTask = new TransferBudgetsTask(this, currentYear, currentMonth-1);
            transferBudgetsTask.execute();
        } else if(id == R.id.filter) {
            PopupMenu popup = new PopupMenu(this, findViewById(R.id.filter));
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_main, popup.getMenu());
            popup.getMenu().clear();

            Tag allTag = new Tag();
            allTag.setName(strAll);

            List<Tag> tagList = new ArrayList<>(Tag.getTagList());
            tagList.add(0, allTag);

            for (final Tag t : tagList) {
                popup.getMenu().add(t.getName()).setOnMenuItemClickListener(new TagFilterMenuItem(t));
            }
            popup.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    class PaymentItemOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Payment payment = (Payment) paymentListView.getItemAtPosition(position);

            Dialog paymentDetailsDialog = new Dialog(MainActivity.this);
            paymentDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            paymentDetailsDialog.setContentView(R.layout.payment_details_dialog);
            paymentDetailsDialog.setTitle(payment.getName());

            setTextOfTextView(paymentDetailsDialog, R.id.paymentDetailPaymentName, payment.getName());
            setTextOfTextView(paymentDetailsDialog, R.id.paymentDetailPaymentAmount, payment.getAmount().doubleValue() + " TL");
            setTextOfTextView(paymentDetailsDialog, R.id.paymentDetailPaymentDate, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(payment.getDate()));
            setTextOfTextView(paymentDetailsDialog, R.id.paymentDetailPaymentTags, Tag.getTag(payment.getTagId()).getName());

            paymentDetailsDialog.show();
        }

        private void setTextOfTextView(Dialog paymentDetailsDialog, int viewId, String text) {
            TextView textView = ButterKnife.findById(paymentDetailsDialog, viewId);
            textView.setText(text);
        }
    }

    class PaymentItemOnLongClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final Payment payment = (Payment) paymentListView.getItemAtPosition(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            LayoutInflater inflater = MainActivity.this.getLayoutInflater();

            View paymentDetail = inflater.inflate(R.layout.payment_deletion_dialog, null);
            TextView textView = ButterKnife.findById(paymentDetail, R.id.pddPaymentDetail);
            textView.setText(payment.getName() + "\n" + payment.getAmount().doubleValue() + " TL\n" + payment.getMonth() + "/" + payment.getYear());

            builder.setView(paymentDetail)
                    .setMessage(strItWillBeDeleted)
                    .setMessage(strAreYouSureToDelete)
                    .setPositiveButton(strYes, new ConfirmClickListener(payment))
                    .setNegativeButton(strNo, null);

            builder.create().show();

            return true;
        }
    }

    class ConfirmClickListener implements DialogInterface.OnClickListener {

        private Payment payment;

        public ConfirmClickListener(Payment payment) {
            this.payment = payment;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            DeletePaymentTask deletePaymentTask = new DeletePaymentTask(MainActivity.this, payment.getId());
            deletePaymentTask.execute();
        }

    }

    class TagFilterMenuItem implements MenuItem.OnMenuItemClickListener {
        private Tag t;

        TagFilterMenuItem(Tag t) {
            this.t = t;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String tagName = item.getTitle().toString();

            if (tagName.equals(strAll)) {
                MainActivity.this.selectedTagId = null;
                MainActivity.this.retrievePaymentList();
            } else {
                MainActivity.this.selectedTagId = t.getId();
                MainActivity.this.retrievePaymentList();
            }
            return true;
        }
    }

}
