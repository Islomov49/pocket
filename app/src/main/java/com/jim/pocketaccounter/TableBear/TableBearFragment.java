package com.jim.pocketaccounter.TableBear;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.credit.ReckingCredit;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 6/9/2016.
 */

public class TableBearFragment extends Fragment {

    private ArrayList<KirimChiqim> kirimChiqims;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_info_debt_borrow_layout, container, false);

        FinanceManager manager = PocketAccounter.financeManager;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        kirimChiqims = new ArrayList<>();

        ArrayList<DebtBorrow> debtBorrows = manager.getDebtBorrows();
        ArrayList<FinanceRecord> financeRecords = manager.getRecords();

        Calendar startDate = Calendar.getInstance();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

        ArrayList<Calendar> dayOf = new ArrayList<>();

        // --------------------bu yerda debtBorrow tekshirildi ----------------
        for (int i = 0; i < debtBorrows.size(); i++) {
            dayOf.add((Calendar) debtBorrows.get(i).getTakenDate().clone());
            for (int j = 0; j < debtBorrows.get(i).getReckings().size(); j++) {
                String date = debtBorrows.get(i).getReckings().get(j).getPayDate();
                try {
                    calendar.setTime(dateFormat1.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dayOf.add((Calendar) calendar.clone());
            }
        }

        Log.d("sinov", "sdasdas");
        //-------------------bu yerda finance records tekshirildi --------------
        for (int i = 0; i < financeRecords.size(); i++) {
            dayOf.add((Calendar) financeRecords.get(i).getDate().clone());
        }

        //----------------- bu yerda Credit details tekshirildi ----------------
        for (int i = 0; i < manager.getCredits().size(); i++) {
            Calendar cal = Calendar.getInstance();
            for (ReckingCredit reckingCredit : manager.getCredits().get(i).getReckings()) {
                cal.setTimeInMillis(reckingCredit.getPayDate());
                dayOf.add(cal);
            }
        }

        //----------------- bir hildagi sanalarni o'chirib chiqish -------------
        for (int i = 0; i < dayOf.size() - 1; i++) {
            dayOf.get(i).set(Calendar.HOUR_OF_DAY, 0);
            dayOf.get(i).set(Calendar.MINUTE, 0);
            dayOf.get(i).set(Calendar.SECOND, 0);
            dayOf.get(i).set(Calendar.MILLISECOND, 0);

            for (int j = i + 1; j < dayOf.size(); j++) {
                dayOf.get(j).set(Calendar.HOUR_OF_DAY, 0);
                dayOf.get(j).set(Calendar.MINUTE, 0);
                dayOf.get(j).set(Calendar.SECOND, 0);
                dayOf.get(j).set(Calendar.MILLISECOND, 0);

                if (dayOf.get(i).compareTo(dayOf.get(j)) == 0) {
                    dayOf.remove(dayOf.get(j));
                }
            }
        }

        //------------------- date larni o'sish tartibida saralash ------
        for (int i = 0; i < dayOf.size() - 1; i++) {
            for (int j = i + 1; j < dayOf.size(); j++) {
                if (dayOf.get(i).compareTo(dayOf.get(j)) >= 0) {
                    Calendar temp = (Calendar) dayOf.get(i).clone();
                    dayOf.add(i, (Calendar) dayOf.get(j).clone());
                    dayOf.add(j, temp);
                    dayOf.remove(i+1);
                    dayOf.remove(j+1);
                }
            }
        }

        Log.d("my", "" + dayOf.size());

        for (int i = 0; i < dayOf.size(); i++) {
            Log.d("myTag", "" + dayOf.get(i).get(Calendar.DAY_OF_MONTH)
                    + " " + dayOf.get(i).get(Calendar.MONTH)
                    + " " + dayOf.get(i).get(Calendar.YEAR));
        }

        //------------------- har bir date ni ko'rib chiqib listga tashlash -------
        for (Calendar days : dayOf) {
            double chiqim = 0;
            double kirim = 0;
            days.set(Calendar.HOUR_OF_DAY, 0);
            days.set(Calendar.MINUTE, 0);
            days.set(Calendar.SECOND, 0);
            days.set(Calendar.MILLISECOND, 0);

            Map<String, Double> chiqims = new HashMap<>();
            Map<String, Double> kirims = new HashMap<>();

            //----------------- Start Credit Details -----------------------
            for (int i = 0; i < manager.getCredits().size(); i++) {
                for (int j = 0; j < manager.getCredits().get(i).getReckings().size(); j++) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(manager.getCredits().get(i).getReckings().get(j).getPayDate());
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);

                    if (days.compareTo(cal) == 0 && manager.getCredits().get(i).isKey_for_include()) {
                        if (chiqims.get("credit pay") != null) {
                            chiqims.put("credit pay", chiqims.get("credit pay") +
                                    manager.getCredits().get(i).getReckings().get(j).getAmount());
                        } else {
                            chiqims.put("credit pay", manager.getCredits().get(i).getReckings().get(j).getAmount());
                        }
                    }
                }
            }

            for (int i = 0; i < debtBorrows.size(); i++) {
                Calendar debtCalendar = debtBorrows.get(i).getTakenDate();
                debtCalendar.set(Calendar.HOUR_OF_DAY, 0);
                debtCalendar.set(Calendar.MINUTE, 0);
                debtCalendar.set(Calendar.SECOND, 0);
                debtCalendar.set(Calendar.MILLISECOND, 0);

                if (days.compareTo(debtCalendar) == 0 && debtBorrows.get(i).isCalculate()) {
                    if (debtBorrows.get(i).getType() == DebtBorrow.DEBT) {
                        if (chiqims.get("qarz oldim") != null) {
                            chiqims.put("qarz oldim", chiqims.remove("qarz oldim") + debtBorrows.get(i).getAmount());
                        } else {
                            chiqims.put("qarz oldim", debtBorrows.get(i).getAmount());
                        }
                        chiqim += debtBorrows.get(i).getAmount();

                    } else {
                        if (chiqims.get("qarz berdim") != null) {
                            chiqims.put("qarz berdim", chiqims.remove("qarz berdim") + debtBorrows.get(i).getAmount());
                        } else {
                            chiqims.put("qarz berdim", debtBorrows.get(i).getAmount());
                        }
                        kirim += debtBorrows.get(i).getAmount();
                    }
                    Log.d("kk", dateFormat.format(startDate.getTime()));
                }

//                 Reckingni tekshirdim
                for (int j = 0; j < debtBorrows.get(i).getReckings().size(); j++) {
                    String date = debtBorrows.get(i).getReckings().get(j).getPayDate();
                    try {
                        calendar.setTime(dateFormat.parse(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (days.compareTo(calendar) == 0 && debtBorrows.get(i).isCalculate()) {
                        if (debtBorrows.get(i).getType() == DebtBorrow.BORROW) {
                            if (chiqims.get("qarz oldim") != null) {
                                chiqims.put("qarz oldim", chiqims.remove("qarz oldim") + debtBorrows.get(i).getReckings().get(j).getAmount());
                            } else {
                                chiqims.put("qarz oldim", debtBorrows.get(i).getReckings().get(j).getAmount());
                            }
                            chiqim += debtBorrows.get(i).getReckings().get(j).getAmount();
                        } else {
                            if (chiqims.get("qarz berdim") != null) {
                                chiqims.put("qarz berdim", chiqims.remove("qarz berdim") + debtBorrows.get(i).getReckings().get(j).getAmount());
                            } else {
                                chiqims.put("qarz berdim", debtBorrows.get(i).getReckings().get(j).getAmount());
                            }
                            kirim += debtBorrows.get(i).getReckings().get(j).getAmount();
                        }
                        Log.d("tek", dateFormat.format(startDate.getTime()));
                    }
                }
            }
//             ------------------ End Debt Borrow -------------------------------//

//             ------------------ Start Finance Record --------------------------//
            for (int i = 0; i < financeRecords.size(); i++) {
                if (financeRecords.get(i).getDate().compareTo(days) == 0) {
                    if (financeRecords.get(i).getCategory().getType() == PocketAccounterGeneral.EXPANCE) {
                        chiqims.put("" + financeRecords.get(i).getCategory().getName()
                                        + " " + financeRecords.get(i).getSubCategory().getName()
                                        + " " + financeRecords.get(i).getAccount().getName(),
                                PocketAccounterGeneral.getCost(financeRecords.get(i)));
                        chiqim += PocketAccounterGeneral.getCost(financeRecords.get(i));
                    } else {
                        kirims.put("" + financeRecords.get(i).getCategory().getName()
                                        + " " + financeRecords.get(i).getSubCategory().getName()
                                        + " " + financeRecords.get(i).getAccount().getName(),
                                PocketAccounterGeneral.getCost(financeRecords.get(i)));
                        kirim += PocketAccounterGeneral.getCost(financeRecords.get(i));
                    }
                }
            }

            if (kirims.size() != 0 || chiqims.size() != 0) {
                kirimChiqims.add(new KirimChiqim(days, kirims, chiqims, kirim - chiqim));
                Log.d("kunlar", "" + dateFormat.format(days.getTime()) + " " + kirim + " " + chiqim);
            }
        }

        return view;
    }
}