package com.quicklib.android.core.helper;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {


    public static TimeZone getDefaultTimeZone(){
        return TimeZone.getDefault();
    }

    public static DateFormat getDefaultDateFormat(){
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();
        dateFormat.setTimeZone(getDefaultTimeZone());
        return dateFormat;
    }

    public static DateFormat getDefaultDateFormat(String pattern){
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        dateFormat.setTimeZone(getDefaultTimeZone());
        return dateFormat;
    }

    public static DateFormat getDefaultDateFormat(String pattern, Locale locale){
        DateFormat dateFormat = new SimpleDateFormat(pattern, locale);
        dateFormat.setTimeZone(getDefaultTimeZone());
        return dateFormat;
    }


    public static Calendar getDefaultCalendarInstance(){
        return GregorianCalendar.getInstance(getDefaultTimeZone());
    }

    public static  Calendar getDefaultCalendarInstance(Date date){
        Calendar workCalendar = getDefaultCalendarInstance();
        workCalendar.setTime(date);
        return workCalendar;
    }

    public static  Calendar getDefaultCalendarInstance(long timestamp){
        Calendar workCalendar = getDefaultCalendarInstance();
        workCalendar.setTimeInMillis(timestamp);
        return workCalendar;
    }





    public static boolean isSameMonth(Date date1, Date date2) {
        return isSameMonth(date1, date2, TimeZone.getDefault());
    }

    public static boolean isSameMonth(Date date1, Date date2, TimeZone timeZone) {
        Calendar calendar1 = Calendar.getInstance(timeZone);
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance(timeZone);
        calendar2.setTime(date2);
        return isSameMonth(calendar1, calendar2);
    }

    public static boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        return isSameDay(date1, date2, TimeZone.getDefault());
    }

    public static boolean isSameDay(Date date1, Date date2, TimeZone timeZone) {
        Calendar calendar1 = Calendar.getInstance(timeZone);
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance(timeZone);
        calendar2.setTime(date2);
        return isSameDay(calendar1, calendar2);
    }

    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isToday(Date date){
        return isToday(date, TimeZone.getDefault());
    }

    public static boolean isToday(Date date, TimeZone timeZone){
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return isToday(calendar);
    }

    public static boolean isToday(Calendar calendar){
        Calendar today = Calendar.getInstance(calendar.getTimeZone());
        return isSameDay(calendar, today);
    }


}
