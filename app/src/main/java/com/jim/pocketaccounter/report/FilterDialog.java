package com.jim.pocketaccounter.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.report.FilterSelectable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by user on 6/15/2016.
 */

public class FilterDialog extends Dialog implements AdapterView.OnItemSelectedListener{
    private static final int START_CURRENT_MONTH = 0;
    private static final int START_3_DAY = 1;
    private static final int START_7_DAY = 2;
    private static final int YEAR_MONTH = 3;
    private static final int YEAR = 4;
    private static final int START_END_TIME = 5;

    private Calendar beginDate;
    private Calendar endDate;

    private Spinner yilFilter;
    private Spinner monthFilter;

    private Spinner yearFilter;

    private EditText startTimeFilter;
    private EditText endTimeFilter;

    private Spinner spinner;

    private ImageView saveBt;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private FilterSelectable filterSelectable = null;

    public FilterDialog(Context context) {
        super(context);
    }

    public FilterDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FilterDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    DatePickerDialog.OnDateSetListener getBeginListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;
            beginDate.set(arg1, arg2, arg3);
            if (beginDate.compareTo(endDate) >= 0) {
                beginDate = Calendar.getInstance();
            }
            startTimeFilter.setText(arg3 + "-" + arg2 + "-" + arg1);
        }
    };

    DatePickerDialog.OnDateSetListener getEndListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;
            endDate.set(arg1, arg2, arg3);
            if (beginDate.compareTo(endDate) >= 0) {
                endDate = (Calendar) beginDate.clone();
            }
            endTimeFilter.setText(arg3 + "-" + arg2 + "-" + arg1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_statistic_layout);
        yearFilter = (Spinner) findViewById(R.id.etFilterStatisticYear);

        yilFilter = (Spinner) findViewById(R.id.spFilterStatisticYearMont);
        monthFilter = (Spinner) findViewById(R.id.spFilterStatisticMonth);

        startTimeFilter = (EditText) findViewById(R.id.etFilterStatisticStartTime);
        endTimeFilter = (EditText) findViewById(R.id.etFilterStatisticEndTime);

        spinner = (Spinner) findViewById(R.id.spFilerStatistic);
        saveBt = (ImageView) findViewById(R.id.ivFilterStatisticSave);

        findViewById(R.id.ivFilterStatisticCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ----- Cancel ------
                dismiss();
            }
        });
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ----- Save -------
                if(spinner.getSelectedItemPosition() == 0){
                    beginDate = (Calendar) Calendar.getInstance().clone();
                    endDate = (Calendar) Calendar.getInstance().clone();

                    beginDate.set(Calendar.DAY_OF_MONTH, 1);
                    beginDate.set(Calendar.HOUR_OF_DAY, 0);
                    beginDate.set(Calendar.MINUTE, 0);
                    beginDate.set(Calendar.SECOND, 0);
                    beginDate.set(Calendar.MILLISECOND, 0);
                }
                filterSelectable.onDateSelected(beginDate, endDate);
                dismiss();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, new String[] {"shu oy", "last 3 day", "last 7 day", "oy yil", "yil", "between date"});

        String[] year = new String[51];
        for (int i = 0; i < 51; i++) {
            year[i] = (i+2000) + "";
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, year);
        String [] months = getContext().getResources().getStringArray(R.array.months);
        ArrayAdapter<String> yearMonthAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, months);

        yearFilter.setAdapter(yearAdapter);
        yilFilter.setAdapter(yearAdapter);
        monthFilter.setAdapter(yearMonthAdapter);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);

        for (int i = 0; i < year.length; i++) {
            if (year[i].matches("" + Calendar.getInstance().get(Calendar.YEAR))) {
                yearFilter.setSelection(i);
                yilFilter.setSelection(i);
            }
        }

        for (int i = 0; i < months.length; i++) {
            if (i == Calendar.getInstance().get(Calendar.MONTH)) {
                monthFilter.setSelection(i);
            }
        }

        spinner.setOnItemSelectedListener(this);

        yearFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    beginDate.setTime(dateFormat.parse("01."+"01."+yearFilter.getSelectedItem()));
                    Toast.makeText(getContext(), "" + yearFilter.getSelectedItem(), Toast.LENGTH_SHORT).show();
                    beginDate.set(Calendar.HOUR_OF_DAY, 0);
                    beginDate.set(Calendar.MINUTE, 0);
                    beginDate.set(Calendar.SECOND, 0);
                    beginDate.set(Calendar.MILLISECOND, 0);

                    endDate = (Calendar) beginDate.clone();
                    endDate.set(Calendar.MONTH, Calendar.DECEMBER);
                    endDate.set(Calendar.DAY_OF_MONTH, 31);
                    endDate.set(Calendar.HOUR_OF_DAY, 23);
                    endDate.set(Calendar.MINUTE, 59);
                    endDate.set(Calendar.SECOND, 59);
                    endDate.set(Calendar.MILLISECOND, 59);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yilFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    beginDate.setTime(dateFormat.parse("01."
                            +(monthFilter.getSelectedItemPosition()+1)
                            +"."+yilFilter.getSelectedItem()));
                    beginDate.set(Calendar.HOUR_OF_DAY, 0);
                    beginDate.set(Calendar.MINUTE, 0);
                    beginDate.set(Calendar.SECOND, 0);
                    beginDate.set(Calendar.MILLISECOND, 0);

                    endDate = (Calendar) beginDate.clone();
                    endDate.set(Calendar.DAY_OF_MONTH, beginDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDate.set(Calendar.HOUR_OF_DAY, 23);
                    endDate.set(Calendar.MINUTE, 59);
                    endDate.set(Calendar.SECOND, 59);
                    endDate.set(Calendar.MILLISECOND, 59);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    beginDate.setTime(dateFormat.parse("01."
                            +(monthFilter.getSelectedItemPosition()+1)
                            +"."+yilFilter.getSelectedItem()));
                    beginDate.set(Calendar.HOUR_OF_DAY, 0);
                    beginDate.set(Calendar.MINUTE, 0);
                    beginDate.set(Calendar.SECOND, 0);
                    beginDate.set(Calendar.MILLISECOND, 0);

                    endDate = (Calendar) beginDate.clone();
                    endDate.set(Calendar.DAY_OF_MONTH, beginDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDate.set(Calendar.HOUR_OF_DAY, 23);
                    endDate.set(Calendar.MINUTE, 59);
                    endDate.set(Calendar.SECOND, 59);
                    endDate.set(Calendar.MILLISECOND, 59);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startTimeFilter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            getBeginListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                    return true;
                }
                return false;
            }
        });
        endTimeFilter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar calendar = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            getEndListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //--------- item selection Events ---------
        switch (position) {
            case START_CURRENT_MONTH: {
                yilFilter.setVisibility(View.INVISIBLE);
                monthFilter.setVisibility(View.INVISIBLE);
                yearFilter.setVisibility(View.INVISIBLE);
                startTimeFilter.setVisibility(View.INVISIBLE);
                endTimeFilter.setVisibility(View.INVISIBLE);

                beginDate = (Calendar) Calendar.getInstance().clone();
                endDate = (Calendar) Calendar.getInstance().clone();

                beginDate.set(Calendar.DAY_OF_MONTH, 1);
                beginDate.set(Calendar.HOUR_OF_DAY, 0);
                beginDate.set(Calendar.MINUTE, 0);
                beginDate.set(Calendar.SECOND, 0);
                beginDate.set(Calendar.MILLISECOND, 0);

                break;
            }
            case START_3_DAY: {
                yilFilter.setVisibility(View.INVISIBLE);
                monthFilter.setVisibility(View.INVISIBLE);
                yearFilter.setVisibility(View.INVISIBLE);
                startTimeFilter.setVisibility(View.INVISIBLE);
                endTimeFilter.setVisibility(View.INVISIBLE);

                beginDate = (Calendar) Calendar.getInstance().clone();
                endDate = (Calendar) Calendar.getInstance().clone();

                beginDate.set(Calendar.DAY_OF_YEAR, endDate.get(Calendar.DAY_OF_YEAR) - 2);
                beginDate.set(Calendar.HOUR_OF_DAY, 0);
                beginDate.set(Calendar.MINUTE, 0);
                beginDate.set(Calendar.SECOND, 0);
                beginDate.set(Calendar.MILLISECOND, 0);

                break;
            }
            case START_7_DAY: {
                yilFilter.setVisibility(View.INVISIBLE);
                monthFilter.setVisibility(View.INVISIBLE);
                yearFilter.setVisibility(View.INVISIBLE);
                startTimeFilter.setVisibility(View.INVISIBLE);
                endTimeFilter.setVisibility(View.INVISIBLE);

                beginDate.set(Calendar.DAY_OF_YEAR, endDate.get(Calendar.DAY_OF_YEAR) - 6);
                beginDate.set(Calendar.HOUR_OF_DAY, 0);
                beginDate.set(Calendar.MINUTE, 0);
                beginDate.set(Calendar.SECOND, 0);
                beginDate.set(Calendar.MILLISECOND, 0);

                break;
            }
            case YEAR_MONTH: {
                yilFilter.setVisibility(View.VISIBLE);
                monthFilter.setVisibility(View.VISIBLE);

                yearFilter.setVisibility(View.INVISIBLE);
                startTimeFilter.setVisibility(View.INVISIBLE);
                endTimeFilter.setVisibility(View.INVISIBLE);

                break;
            }
            case YEAR: {
                yearFilter.setVisibility(View.VISIBLE);

                yilFilter.setVisibility(View.INVISIBLE);
                monthFilter.setVisibility(View.INVISIBLE);
                startTimeFilter.setVisibility(View.INVISIBLE);
                endTimeFilter.setVisibility(View.INVISIBLE);

                break;
            }
            case START_END_TIME: {
                startTimeFilter.setVisibility(View.VISIBLE);
                endTimeFilter.setVisibility(View.VISIBLE);

                yilFilter.setVisibility(View.INVISIBLE);
                monthFilter.setVisibility(View.INVISIBLE);
                yearFilter.setVisibility(View.INVISIBLE);
                beginDate = (Calendar) Calendar.getInstance().clone();
                endDate = (Calendar) Calendar.getInstance().clone();

                startTimeFilter.setText(dateFormat.format(beginDate.getTime()));
                endTimeFilter.setText(dateFormat.format(endDate.getTime()));

                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setOnDateSelectedListener (FilterSelectable filterSelectable) {
        this.filterSelectable = filterSelectable;
    }
}
