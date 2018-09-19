package com.u2h.user.united2healandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialog extends DialogFragment {
    public String date;

    public interface DialogListener{
        public void onPositiveClick(CalendarDialog dialog);
        public void onNegativeClick(CalendarDialog dialog);
    }
    DialogListener mListener;

    @Override
    public void onAttach(Context context) {
        mListener=(DialogListener) context;
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.calendar_dialog,null);
         CalendarView calendar= view.findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                date=month+"/"+day+"/"+year;

            }
        });
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onPositiveClick(CalendarDialog.this);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        return builder.create();
    }
}
