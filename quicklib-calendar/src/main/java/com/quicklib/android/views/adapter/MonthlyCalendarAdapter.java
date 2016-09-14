package com.quicklib.android.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public abstract class MonthlyCalendarAdapter<CVH extends RecyclerView.ViewHolder, HVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter  {

    private static final int ITEM_VIEW_TYPE_CELL = 0;
    private static final int ITEM_VIEW_TYPE_HEADER = -1;
    private static final int DAYS_IN_WEEK = 7;


    private final Calendar mainCalendar;
    private final List<Date> dateList;
    private boolean showHeaderRow = true;

    protected MonthlyCalendarAdapter(Calendar calendar){
        dateList = new ArrayList<>();
        mainCalendar = (Calendar) calendar.clone();

        Calendar workCalendar = (Calendar) mainCalendar.clone();

        // determine the cell for current month's beginning
        workCalendar.set(Calendar.DAY_OF_MONTH, 1);
        workCalendar.set(Calendar.HOUR_OF_DAY, 12);
        workCalendar.set(Calendar.MINUTE, 0);
        int monthBeginningCell = 0;
        if (workCalendar.get(Calendar.DAY_OF_WEEK) < workCalendar.getFirstDayOfWeek()) {
            monthBeginningCell = 7 - workCalendar.get(Calendar.DAY_OF_WEEK);
        } else {
            monthBeginningCell = workCalendar.get(Calendar.DAY_OF_WEEK) - workCalendar.getFirstDayOfWeek();
        }

        // move calendar backwards to the beginning of the week
        workCalendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        dateList.add(workCalendar.getTime());
        workCalendar.add(Calendar.DATE, 1);
        //noinspection ResourceType
        while( isSameMonth(mainCalendar, workCalendar) || workCalendar.getFirstDayOfWeek() != workCalendar.get(Calendar.DAY_OF_WEEK) ){
            dateList.add(workCalendar.getTime());
            workCalendar.add(Calendar.DATE, 1);
        }

    }


    public void showHeaderRow(boolean showHeaderRow) {
        this.showHeaderRow = showHeaderRow;
        this.notifyDataSetChanged();
    }

    public void nextDay(){
        mainCalendar.add(Calendar.DATE, 1);
        notifyDataSetChanged();
    }

    public void previousDay(){
        mainCalendar.add(Calendar.DATE, -1);
        notifyDataSetChanged();
    }

    public void nextMonth(){
        mainCalendar.add(Calendar.MONTH, 1);
        notifyDataSetChanged();
    }

    public void previousMonth(){
        mainCalendar.add(Calendar.MONTH, -1);
        notifyDataSetChanged();
    }

    public void nextYear(){
        mainCalendar.add(Calendar.YEAR, 1);
        notifyDataSetChanged();
    }

    public void previousYear(){
        mainCalendar.add(Calendar.YEAR, -1);
        notifyDataSetChanged();
    }




    @Override
    public final int getItemViewType(int position) {
        return (showHeaderRow && ( position < DAYS_IN_WEEK )) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_CELL;
    }

    @Override
    public final int getItemCount() {
        return showHeaderRow ? dateList.size() + DAYS_IN_WEEK : dateList.size();
    }


    public abstract HVH onCreateHeaderViewHolder(ViewGroup parent);
    public abstract CVH onCreateCellViewHolder(ViewGroup parent);
    public abstract void onBindHeaderViewHolder(HVH holder, Date date);
    public abstract void onBindCellViewHolder(CVH holder, Date date, boolean isDayOfMonth);

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        switch(viewType){
            case ITEM_VIEW_TYPE_HEADER:
                return onCreateHeaderViewHolder(parent);
            case ITEM_VIEW_TYPE_CELL:
            default:
                return onCreateCellViewHolder(parent);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Date date = getDate(position);
        switch(getItemViewType(position)){
            case ITEM_VIEW_TYPE_HEADER:
                onBindHeaderViewHolder((HVH)holder, date );
                break;
            case ITEM_VIEW_TYPE_CELL:
            default:
                onBindCellViewHolder((CVH) holder, date, isSameMonth(mainCalendar.getTime(), date) );
                break;
        }
    }

    private Date getDate(int position) {
        if(showHeaderRow){
            if( position < DAYS_IN_WEEK ){
                return dateList.get(position);
            }else{
                return dateList.get(position - DAYS_IN_WEEK);
            }
        }else{
            return dateList.get(position);
        }
    }

    protected Calendar getCalendarInstance(){
        return (Calendar) mainCalendar.clone();
    }

    protected Calendar getCalendarInstance(Date date){
        Calendar workCalendar = (Calendar) mainCalendar.clone();
        workCalendar.setTime(date);
        return workCalendar;
    }

    protected Calendar getCalendarInstance(long timestamp){
        Calendar workCalendar = (Calendar) mainCalendar.clone();
        workCalendar.setTimeInMillis(timestamp);
        return workCalendar;
    }

    private boolean isSameMonth(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }


    private boolean isSameMonth(Date date1, Date date2) {
        return isSameMonth(date1, date2, TimeZone.getDefault());
    }

    private boolean isSameMonth(Date date1, Date date2, TimeZone timeZone) {
        Calendar calendar1 = Calendar.getInstance(timeZone);
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance(timeZone);
        calendar2.setTime(date2);
        return isSameMonth(calendar1, calendar2);
    }

}
