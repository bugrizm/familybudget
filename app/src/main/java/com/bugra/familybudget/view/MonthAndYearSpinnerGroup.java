package com.bugra.familybudget.view;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import com.bugra.familybudget.R;
import com.bugra.familybudget.utils.CalendarUtils;

public class MonthAndYearSpinnerGroup {
	
	private Activity activity;
	
	private Spinner monthSpinner;
	private Spinner yearSpinner;

	public MonthAndYearSpinnerGroup(Activity activity, Spinner monthSpinner, Spinner yearSpinner) {
		this.activity = activity;
		this.monthSpinner = monthSpinner;
		this.yearSpinner = yearSpinner;
	}
	
	public void initSpinners() {
        int[] monthYear = CalendarUtils.getCurrentMonthYear();

        initMonthSpinner(monthYear[0]);
        initYearSpinner(monthYear[1]);
	}

	private void initMonthSpinner(int month) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity, R.array.months_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinner.setAdapter(adapter);
		
		monthSpinner.setSelection(month-1);
	}
	
	private void initYearSpinner(int year) {
		List<Integer> list = CalendarUtils.getYearList();
		
		ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(activity, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinner.setAdapter(dataAdapter);
		
		yearSpinner.setSelection(list.indexOf(year));
	}
	
	public Integer getMonth() {
		return monthSpinner.getSelectedItemPosition();
	}
	
	public Integer getYear() {
		return Integer.parseInt(yearSpinner.getSelectedItem().toString());
	}
}
