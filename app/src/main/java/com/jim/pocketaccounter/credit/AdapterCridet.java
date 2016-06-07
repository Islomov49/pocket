package com.jim.pocketaccounter.credit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.pocketaccounter.AddCreditFragment;
import com.jim.pocketaccounter.InfoCreditFragment;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by developer on 02.06.2016.
 */

public class AdapterCridet extends RecyclerView.Adapter<AdapterCridet.myViewHolder>{
   List<CreditComputeDate> cardDetials;
    int S = 0;
    Context This;
    public AdapterCridet(List<CreditComputeDate> cardDetials, Context This){
       this.cardDetials=cardDetials;
        this.This=This;
    }


    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
    final CreditComputeDate itemCr= cardDetials.get(position);
        String valyut=itemCr.getValuta();
    holder.nameCr.setText(itemCr.getNameCredit());
    holder.overallCr.setText(Double.toString(itemCr.getOverallDebt())+valyut);
    holder.periodPayCr.setText(Double.toString(itemCr.getPeriodPayed())+valyut);
    holder.shouldCr.setText(Double.toString(itemCr.getShouldPay())+valyut);
    holder.totalPaidCr.setText(Double.toString(itemCr.getTotalPaid())+valyut);

        Date AAa = (new Date());
        AAa.setTime(itemCr.getPerionEndsInMilisec());
    holder.periodEndCr.setText((new SimpleDateFormat("dd.MM.yyyy")).format(AAa));

        holder.glav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new InfoCreditFragment(),"infoFrag");

            }
        });
    }

    @Override
    public int getItemCount() {
        return cardDetials.size();
    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cred_titem, parent, false);
        // set the view's size, margins, paddings and layout parameters

        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public TextView nameCr;
        public TextView shouldCr;
        public TextView overallCr;
        public TextView periodEndCr;
        public TextView periodPayCr;
        public TextView totalPaidCr;
        public View glav;
        public myViewHolder(View v) {
            super(v);
            nameCr=(TextView) v.findViewById(R.id.NameCr);
            overallCr=(TextView) v.findViewById(R.id.overallpay);
            periodPayCr=(TextView) v.findViewById(R.id.inperiodpayed);
            totalPaidCr=(TextView) v.findViewById(R.id.totalpayd);
            shouldCr=(TextView) v.findViewById(R.id.shouldpay);
            periodEndCr=(TextView) v.findViewById(R.id.dateends);
            glav= v;
        }
    }
    public void openFragment(Fragment fragment,String tag) {
        if (fragment != null) {
            final FragmentTransaction ft = ((PocketAccounter)This).getSupportFragmentManager().beginTransaction().addToBackStack(tag);
            ft.add(R.id.flMain, fragment,tag);

            ft.commit();
        }
    }
}
