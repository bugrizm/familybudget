package com.bugra.familybudget.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.bugra.familybudget.R;
import com.bugra.familybudget.entity.Payment;
import com.bugra.familybudget.entity.Tag;
import com.bugra.familybudget.view.PaymentIconView;

import butterknife.ButterKnife;

public class PaymentListAdapter extends BaseAdapter {

    private List<Payment> paymentList;
    private LayoutInflater inflater;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static PaymentListAdapter newInstance(LayoutInflater inflater, List<Payment> paymentList) {
        PaymentListAdapter adapter = new PaymentListAdapter();
        adapter.inflater = inflater;
        adapter.paymentList = paymentList;
        return adapter;
    }

    @Override
    public int getCount() {
        return paymentList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return paymentList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_list_item, parent, false);
        }

        Payment payment = paymentList.get(position);

        updatePaymentNameTextView(convertView, payment.getName());
        updatePaymentAmountTextView(convertView, payment.getAmount().doubleValue());
        updatePaymentDateTextView(convertView, payment.getDate());
        updatePaymentIcon(convertView, payment.getTagId());

        return convertView;
    }

    private void updatePaymentNameTextView(View view, String paymentName) {
        TextView paymentNameTextView = ButterKnife.findById(view, R.id.pliName);
        paymentNameTextView.setText(paymentName);
    }

    private void updatePaymentAmountTextView(View view, double paymentAmount) {
        TextView paymentAmountTextView = ButterKnife.findById(view, R.id.pliAmount);

        int textColor =  getPaymentAmountTextColor(paymentAmount);
        paymentAmountTextView.setText(Math.abs(paymentAmount) + " TL");
        paymentAmountTextView.setTextColor(textColor);
    }

    private int getPaymentAmountTextColor(double paymentAmount) {
        return paymentAmount<0 ? Color.RED : Color.rgb(0, 125, 0);
    }

    private void updatePaymentDateTextView(View view, Date paymentDate) {
        TextView paymentDateTextView = ButterKnife.findById(view, R.id.pliDate);
        paymentDateTextView.setText(format.format(paymentDate));
    }

    private void updatePaymentIcon(View view, int paymentTagId) {
        PaymentIconView paymentIcon = ButterKnife.findById(view, R.id.paymentIcon);
        TextView paymentIconText = ButterKnife.findById(view, R.id.paymentIconTextView);

        Tag tag = Tag.getTag(paymentTagId);
        if(tag != null) {
            paymentIcon.setColor(tag.getColor());
            paymentIconText.setText(tag.getIconText());
        }
    }

}
