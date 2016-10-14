package com.bugra.familybudget.component;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.bugra.familybudget.R;
import com.bugra.familybudget.entity.PaymentType;

public class PaymentTypeSelector {

    private Button selectedButton;

    private TextView installmentAmountTextView;

    private Context context;

    private Map<Button, PaymentType> buttonPaymentTypeMap;

    public PaymentTypeSelector(Context context, TextView installmentAmountTextView) {
        this.context = context;
        buttonPaymentTypeMap = new HashMap();
        this.installmentAmountTextView = installmentAmountTextView;
    }

    public void addButton(Button button, PaymentType paymentType) {
        buttonPaymentTypeMap.put(button, paymentType);
        setButtonAsUnselected(button);
    }

    public void selectButton(Button selectedButton) {
        //set all buttons as unselected then change the given button as selected
        for (Button button : buttonPaymentTypeMap.keySet()) {
            setButtonAsUnselected(button);
        }

        setButtonAsSelected(selectedButton);

        setVisibilityOfInstallmentAmountText(selectedButton);
    }

    public PaymentType getSelectedPaymentType() {
        return buttonPaymentTypeMap.get(selectedButton);
    }

    private void setButtonAsSelected(Button button) {
        button.setTextColor(0xFFFFFFFF);
        button.setBackground(context.getResources().getDrawable(R.drawable.blue_button_background));
        selectedButton = button;
    }

    private void setButtonAsUnselected(Button button) {
        button.setTextColor(0xFF64E1F0);
        button.setBackground(context.getResources().getDrawable(R.drawable.white_button_background));
    }

    private void setVisibilityOfInstallmentAmountText(Button selectedButton) {
        if(buttonPaymentTypeMap.get(selectedButton) == PaymentType.MULTIPLE) {
            installmentAmountTextView.setVisibility(View.VISIBLE);
        } else {
            installmentAmountTextView.setVisibility(View.INVISIBLE);
        }
    }


}
