package com.jim.pocketaccounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.debt.InfoDebtBorrowFragment;
import com.jim.pocketaccounter.debt.Recking;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.util.ArrayList;

public class InfoCreditFragment extends Fragment {

    private FinanceManager manager;

    public InfoCreditFragment() {
        // Required empty public constructor
    }
    public void shareData(CreditDetials current){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = PocketAccounter.financeManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_info_credit, container, false);
        RecyclerView recyclerView = (RecyclerView) V.findViewById(R.id.recycler_for_transactions);


        V.findViewById(R.id.pustooyy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return V;
    }

    public void onButtonPressed(Uri uri) {

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
