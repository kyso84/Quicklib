package com.quicklib.android.core.helper;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This helper provides convenient methods related to date & time
 *
 * @author Benoit Deschanel
 * @package com.quicklib.android.core.helper
 * @since 17-05-02
 */
public class DateHelper {

    /**
     * Is same month boolean.
     *
     * @param date1    the date 1
     * @param date2    the date 2
     * @param timeZone the time zone
     * @return the boolean
     */
    public static boolean isSameMonth(Date date1, Date date2, TimeZone timeZone) {
        Calendar calendar1 = Calendar.getInstance(timeZone);
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance(timeZone);
        calendar2.setTime(date2);
        return isSameMonth(calendar1, calendar2);
    }

    /**
     * Is same month boolean.
     *
     * @param calendar1 the calendar 1
     * @param calendar2 the calendar 2
     * @return the boolean
     */
    public static boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    /**
     * Is same day boolean.
     *
     * @param date1 the date 1
     * @param date2 the date 2
     * @return the boolean
     */
    public static boolean isSameDay(Date date1, Date date2) {
        return isSameDay(date1, date2, TimeZone.getDefault());
    }

    /**
     * Is same day boolean.
     *
     * @param date1    the date 1
     * @param date2    the date 2
     * @param timeZone the time zone
     * @return the boolean
     */
    public static boolean isSameDay(Date date1, Date date2, TimeZone timeZone) {
        Calendar calendar1 = Calendar.getInstance(timeZone);
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance(timeZone);
        calendar2.setTime(date2);
        return isSameDay(calendar1, calendar2);
    }

    /**
     * Is same day boolean.
     *
     * @param calendar1 the calendar 1
     * @param calendar2 the calendar 2
     * @return the boolean
     */
    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Is today boolean.
     *
     * @param date the date
     * @return the boolean
     */
    public static boolean isToday(Date date){
        return isToday(date, TimeZone.getDefault());
    }

    /**
     * Is today boolean.
     *
     * @param date     the date
     * @param timeZone the time zone
     * @return the boolean
     */
    public static boolean isToday(Date date, TimeZone timeZone){
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(date);
        return isToday(calendar);
    }

    /**
     * Is today boolean.
     *
     * @param calendar the calendar
     * @return the boolean
     */
    public static boolean isToday(Calendar calendar){
        Calendar today = Calendar.getInstance(calendar.getTimeZone());
        return isSameDay(calendar, today);
    }


}
