package com.jim.pocketaccounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.credit.ReckingCredit;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.debt.InfoDebtBorrowFragment;
import com.jim.pocketaccounter.debt.Recking;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InfoCreditFragment extends Fragment {
    ImageView expandableBut;
    FrameLayout expandablePanel;
    FrameLayout expandableLiniya;

    CreditDetials currentCredit;

    TextView myCreditName;
    TextView myLefAmount;
    TextView myProcent;
    TextView myLefDate;
    TextView myPeriodOfCredit;
    TextView myTakedCredTime;
    TextView myTakedValue;
    TextView myReturnValue;
    TextView myTotalPaid;

    final static long forDay=1000L*60L*60L*24L;
    final static long forMoth=1000L*60L*60L*24L*30L;
    final static long forWeek=1000L*60L*60L*24L*7L;
    final static long forYear=1000L*60L*60L*24L*365L;

    SimpleDateFormat dateformarter;
    boolean isExpandOpen=false;
    private FinanceManager manager;

    public InfoCreditFragment() {
        // Required empty public constructor
    }
    public void setConteent(CreditDetials temp){
        currentCredit=temp;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = PocketAccounter.financeManager;
        dateformarter=new SimpleDateFormat("dd.MM.yyyy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_info_credit, container, false);
        Date dateForSimpleDate = (new Date());

        expandableBut=(ImageView) V.findViewById(R.id.wlyuzik_opener);
        expandablePanel=(FrameLayout) V.findViewById(R.id.shlyuzik);
        expandableLiniya=(FrameLayout) V.findViewById(R.id.with_wlyuzik);
         myCreditName=(TextView) V.findViewById(R.id.name_of_credit);
        myLefAmount=(TextView) V.findViewById(R.id.value_credit_all);
        myProcent=(TextView) V.findViewById(R.id.procentCredInfo);
        myLefDate=(TextView) V.findViewById(R.id.leftDateInfo);
        myPeriodOfCredit=(TextView) V.findViewById(R.id.intervalCreditInfo);
        myTakedCredTime=(TextView) V.findViewById(R.id.takedtimeInfo);
        myTakedValue=(TextView) V.findViewById(R.id.takedValueInfo);
        myReturnValue=(TextView) V.findViewById(R.id.totalReturnValueInfo);
        myTotalPaid=(TextView) V.findViewById(R.id.total_transaction);



        myTakedValue.setText(parseToWithoutNull(currentCredit.getValue_of_credit())+currentCredit.getValyute_currency().getAbbr());
        myReturnValue.setText(parseToWithoutNull(currentCredit.getValue_of_credit_with_procent())+currentCredit.getValyute_currency().getAbbr());

        dateForSimpleDate.setTime(currentCredit.getTake_time().getTimeInMillis());
        myTakedCredTime.setText(dateformarter.format(dateForSimpleDate));


        myCreditName.setText(currentCredit.getCredit_name());

        double total_paid=0;
        for(ReckingCredit item:currentCredit.getReckings())
            total_paid+=item.getAmount();

        myTotalPaid.setText(parseToWithoutNull(total_paid)+currentCredit.getValyute_currency().getAbbr());
        myLefAmount.setText(parseToWithoutNull(currentCredit.getValue_of_credit_with_procent()-total_paid)+currentCredit.getValyute_currency().getAbbr());

        String suffix="";
        if(currentCredit.getProcent_interval()==forDay){
            suffix=getString(R.string.per_day);
        }
        else if(currentCredit.getProcent_interval()==forWeek){
            suffix=getString(R.string.per_week);
        }
        else if(currentCredit.getProcent_interval()==forMoth){
            suffix=getString(R.string.per_month);
        }
        else {
            suffix=getString(R.string.per_year);
        }

        myProcent.setText(parseToWithoutNull(currentCredit.getProcent())+"%"+" "+suffix);


     //TODO DAvomi qiliw kere


        V.findViewById(R.id.infoooc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpandOpen){
                    expandablePanel.setVisibility(View.GONE);
                    expandableLiniya.setVisibility(View.GONE);
                    expandableBut.setImageResource(R.drawable.infoo);
                    isExpandOpen=false;
                }
                else {
                    expandablePanel.setVisibility(View.VISIBLE);
                    expandableLiniya.setVisibility(View.VISIBLE);
                    expandableBut.setImageResource(R.drawable.pasga_ochil);
                    isExpandOpen=true;
                }
            }
        });


        V.findViewById(R.id.pustooyy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return V;
    }
    public String parseToWithoutNull(double A){
        if(A==(int)A)
            return Integer.toString((int)A);
        else
            return Double.toString(A);

    }









    private class PaysCreditAdapter extends RecyclerView.Adapter<InfoCreditFragment.ViewHolder> {
        private ArrayList<Recking> list;

        public PaysCreditAdapter(ArrayList<Recking> list) {
            this.list = list;
        }

        public int getItemCount() {
            return list.size();
        }

        public void onBindViewHolder(InfoCreditFragment.ViewHolder view, int position) {
            view.infoDate.setText(list.get(position).getPayDate());
            view.infoSumm.setText("" + list.get(position).getAmount());
            view.infoAccount.setText("" + list.get(position).getAccountId());
            view.comment.setText("" + list.get(position).getComment());
        }

        public InfoCreditFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payed_item, parent, false);
            return new ViewHolder(view);
        }

        public void setDataChanged(String clDate, double value) {
            Toast.makeText(getContext(), "212", Toast.LENGTH_LONG).show();
//            for (DebtBorrow debtBorrow : manager.getDebtBorrows()) {
//                if (debtBorrow.getReckings().equals(list)) {
////                    Recking recking = new Recking(clDate, value, debtBorrow.getId());
////                    list.add(0, recking);
////                    double qoldiq = 0;
////                    for (int i = 0; i < list.size(); i++) {
////                        qoldiq += list.get(i).getAmount();
////                    }
//////                    leftAmount.setText(""+ (debtBorrow.getAmount() - qoldiq));
//////                    totalPayAmount.setText("" + qoldiq);
////                    debtBorrow.setReckings(list);
//                    break;
//                }
//            }
//            manager.loadDebtBorrows();
            notifyItemInserted(0);
        }

        public void setRemoveItem(int id) {
//            persons.remove(id);
//            notifyItemRemoved(id);
        }

    }
    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView infoDate;
        public TextView infoSumm;
        public TextView infoAccount;
        public TextView comment;

        public ViewHolder(View view) {
            super(view);
            infoDate = (TextView) view.findViewById(R.id.date_of_trans);
            infoAccount = (TextView) view.findViewById(R.id.via_acount);
            comment = (TextView) view.findViewById(R.id.comment_trans);
            infoSumm = (TextView) view.findViewById(R.id.paid_value);
        }
    }
}
