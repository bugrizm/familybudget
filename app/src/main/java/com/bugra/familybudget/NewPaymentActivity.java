package com.bugra.familybudget;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bugra.familybudget.R;

import com.bugra.familybudget.component.PaymentTypeSelector;
import com.bugra.familybudget.entity.PaymentDTO;
import com.bugra.familybudget.entity.PaymentType;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.http.InsertPaymentTask;
import com.bugra.familybudget.view.MonthAndYearSpinnerGroup;
import com.bugra.familybudget.view.PaymentIconView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewPaymentActivity extends ActionBarActivity {

    public static final int NEW_PAYMENT_ACTIVITY_RESULT = 1;

    private PaymentTypeSelector paymentTypeSelector;
    private MonthAndYearSpinnerGroup monthAndYearSpinnerGroup;

    private Map<PaymentIconView, Tag> toggleButtonTagMap;

    private float maxTagLayoutWidth;
    private float scale;

    private PaymentIconView selectedTagIcon;

    @BindView(R.id.newPaymentLayout) RelativeLayout newPaymentLayout;
    @BindView(R.id.spinnerPaymentMonth) Spinner paymentMonthSpinner;
    @BindView(R.id.spinnerPaymentYear) Spinner paymentYearSpinner;
    @BindView(R.id.textPaymentInstallmentAmount) TextView installmentAmountTextView;
    @BindView(R.id.buttonSinglePayment) Button singlePaymentButton;
    @BindView(R.id.buttonMultiplePayment) Button multiplePaymentButton;
    @BindView(R.id.buttonIncome) Button incomeButton;
    @BindView(R.id.tagLinearLayout) LinearLayout tagLayout;
    @BindView(R.id.textPaymentName) EditText paymentNameText;
    @BindView(R.id.textPaymentAmount) EditText paymentAmountText;

    @BindString(R.string.ok) String strOk;
    @BindString(R.string.tag_not_selected) String strTagNotSelected;
    @BindString(R.string.enter_payment_name) String strEnterPaymentName;
    @BindString(R.string.enter_payment_amount) String strEnterPaymentAmount;
    @BindString(R.string.enter_payment_installment_amount) String strEnterPaymentInstallmentAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);
        ButterKnife.bind(this);

        initPaymentTypeButtons();

        monthAndYearSpinnerGroup = new MonthAndYearSpinnerGroup(this, paymentMonthSpinner, paymentYearSpinner);
        monthAndYearSpinnerGroup.initSpinners();

        FrameLayout frameLayout = (FrameLayout) newPaymentLayout.getParent();
        frameLayout.measure(FrameLayout.LayoutParams.MATCH_PARENT, 0);
        maxTagLayoutWidth = frameLayout.getMeasuredWidth();
        scale = getResources().getDisplayMetrics().density;
        maxTagLayoutWidth = (maxTagLayoutWidth / 3) * scale + 0.5f;

        initTagButtons();
    }

    private void initPaymentTypeButtons() {
        paymentTypeSelector = new PaymentTypeSelector(this, installmentAmountTextView);

        paymentTypeSelector.addButton(singlePaymentButton, PaymentType.SINGLE);
        paymentTypeSelector.addButton(multiplePaymentButton, PaymentType.MULTIPLE);
        paymentTypeSelector.addButton(incomeButton, PaymentType.INCOME);

        paymentTypeSelector.selectButton(singlePaymentButton);
    }

    private void initTagButtons() {
        List<Tag> tagList = Tag.getTagList();
        toggleButtonTagMap = new HashMap<>();
        selectedTagIcon = null;

        LinearLayout currentLinearLayout = null;

        for (int i=0; i<tagList.size(); i++) {
            Tag tag = tagList.get(i);

            if(i%6 == 0) {
                currentLinearLayout = new LinearLayout(getApplicationContext());
                currentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                float calculatedHeight = 50 * scale + 0.5f;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int)calculatedHeight);

                tagLayout.addView(currentLinearLayout, params);
            }

            View tagButtonGroup = getLayoutInflater().inflate(R.layout.new_payment_tag_button, currentLinearLayout, false);

            PaymentIconView paymentIcon = ButterKnife.findById(tagButtonGroup, R.id.paymentIcon);
            TextView paymentIconText = ButterKnife.findById(tagButtonGroup, R.id.paymentIconTextView);

            paymentIcon.setColor(tag.getColor());
            paymentIcon.setTransparent(true);
            paymentIconText.setText(tag.getIconText());
            paymentIcon.setOnClickListener(new PaymentIconOnClickListener());

            toggleButtonTagMap.put(paymentIcon, tag);
            currentLinearLayout.addView(tagButtonGroup);
        }

    }

    public void buttonClicked(View view) {
        paymentTypeSelector.selectButton((Button) view);
    }

    public void saveButtonOnClick(View view) {
        if(selectedTagIcon == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(strTagNotSelected)
                    .setPositiveButton(strOk, null)
                    .create()
                    .show();
        } else {
            addPayment();
        }
    }

    private void addPayment() {
        String totalAmountText = paymentAmountText.getText().toString();
        String paymentName = paymentNameText.getText().toString();

        if(paymentName.equals("")) {
            paymentNameText.setError(strEnterPaymentName);
            return;
        }
        if(totalAmountText.toString().equals("")) {
            paymentAmountText.setError(strEnterPaymentAmount);
            return;
        }

        PaymentDTO newPayment = new PaymentDTO();

        Float totalAmount = Float.parseFloat(totalAmountText) * -1;

        newPayment.setDate(Calendar.getInstance().getTime());
        newPayment.setName(paymentName);
        newPayment.setAmount(new BigDecimal(totalAmount));
        newPayment.setTagId(NewPaymentActivity.this.toggleButtonTagMap.get(selectedTagIcon).getId());
        newPayment.setMonth(monthAndYearSpinnerGroup.getMonth().shortValue());
        newPayment.setYear(monthAndYearSpinnerGroup.getYear().shortValue());

        if(paymentTypeSelector.getSelectedPaymentType() == PaymentType.SINGLE) {
            newPayment.setIsMultiple(false);
        } else if(paymentTypeSelector.getSelectedPaymentType() == PaymentType.MULTIPLE) {
            String installmentAmount = installmentAmountTextView.getText().toString();
            if(installmentAmount.equals("")) {
                installmentAmountTextView.setError(strEnterPaymentInstallmentAmount);
                return;
            }

            newPayment.setIsMultiple(true);
            Integer installmentNumber = Integer.parseInt(installmentAmount);
            newPayment.setInstallmentAmount(installmentNumber);
        } else if (paymentTypeSelector.getSelectedPaymentType() == PaymentType.INCOME) {
            newPayment.setAmount(newPayment.getAmount().multiply(new BigDecimal(-1)));
        }

        InsertPaymentTask insertPaymentTask = new InsertPaymentTask(this, newPayment);
        insertPaymentTask.execute();
    }

    class PaymentIconOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(((PaymentIconView)v).getTransparent()) {
                ((PaymentIconView)v).setTransparent(false);

                final Toast toast = Toast.makeText(NewPaymentActivity.this, NewPaymentActivity.this.toggleButtonTagMap.get(v).getName(), Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 300);

                if(selectedTagIcon != null) {
                    selectedTagIcon.setTransparent(true);
                }

                selectedTagIcon = (PaymentIconView)v;
            } else {
                ((PaymentIconView)v).setTransparent(true);
                selectedTagIcon = null;
            }
        }
    }

}
