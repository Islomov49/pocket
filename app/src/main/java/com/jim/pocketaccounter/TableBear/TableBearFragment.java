package com.jim.pocketaccounter.TableBear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by user on 6/9/2016.
 */

public class TableBearFragment extends Fragment {

    private ArrayList<KirimChiqim> kirimChiqims;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout., container, false);
        FinanceManager manager = PocketAccounter.financeManager;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        kirimChiqims = new ArrayList<>();

        ArrayList<DebtBorrow> debtBorrows = manager.getDebtBorrows();

        Calendar startDate = Calendar.getInstance();

        for (int i = 0; i < debtBorrows.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            for (int j = 0; j < debtBorrows.get(i).getReckings().size(); j++) {
                String date = debtBorrows.get(i).getReckings().get(j).getPayDate();
                int day = Integer.parseInt(date.substring(0, date.indexOf('.')));
                int mounth = Integer.parseInt(date.substring(date.indexOf('.') + 1, date.indexOf('.', date.indexOf('.') + 1)));
                int year = Integer.parseInt(date.substring(date.lastIndexOf('.')+1, date.length()));
                calendar.set(day, mounth, year);
                if (calendar.compareTo(startDate) <= 0) {
                    startDate = calendar;
                }
            }
            if (startDate.compareTo(debtBorrows.get(i).getTakenDate()) <= 0) {
                startDate = debtBorrows.get(i).getTakenDate();
            }
        }

        for (int i = 0; i < debtBorrows.size(); i++) {
            if (startDate.compareTo(debtBorrows.get(i).getTakenDate()) == 0) {
                debtBorrows.get(i).getAmount()
            }
        }

        return new View(getContext());
    }
}