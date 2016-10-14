package com.bugra.familybudget.utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {

    //this could be set by options
    private static final int SALARY_DAY = 13;

    private static List<Integer> yearList = null;

    public static int[] getCurrentMonthYear() {
        DateTime currentDate = new DateTime();

        int dayOfMonth = currentDate.getDayOfMonth();

        if(dayOfMonth > SALARY_DAY) {
            currentDate = currentDate.plusMonths(1);
        }

        return new int[]{currentDate.getMonthOfYear(), currentDate.getYear()};
    }

    public static int[] getPreviousMonthYear(int currentMonth, int currentYear) {
        return addMonth(currentMonth, currentYear, -1);
    }

    public static int[] getNextMonthYear(int currentMonth, int currentYear) {
        return addMonth(currentMonth, currentYear, 1);
    }

    private static int[] addMonth(int currentMonth, int currentYear, int addedMonth) {
        DateTime date = new DateTime();

        date = date.withMonthOfYear(currentMonth).withYear(currentYear).plusMonths(addedMonth);

        return new int[]{date.getMonthOfYear(), date.getYear()};
    }

    public static List<Integer> getYearList() {
        if(yearList == null) {
            createYearList();
        }

        return yearList;
    }

    private static void createYearList() {
        yearList = new ArrayList<Integer>();

        int currentYear = getCurrentMonthYear()[1];
        for(int i=-1; i<5; i++) {
            yearList.add(currentYear + i);
        }

    }

}
