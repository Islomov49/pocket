package com.jim.pocketaccounter.TableBear;

import android.app.Dialog;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by user on 6/15/2016.
 */

public class FilterFialog extends Dialog {
    private Calendar beginDate;
    private Calendar endDate;


    public FilterFialog(Context context) {
        super(context);
    }

    public FilterFialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FilterFialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
