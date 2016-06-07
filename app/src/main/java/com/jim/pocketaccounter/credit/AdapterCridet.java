package com.jim.pocketaccounter.credit;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.pocketaccounter.AddCreditFragment;
import com.jim.pocketaccounter.InfoCreditFragment;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by developer on 02.06.2016.
 */

public class AdapterCridet extends RecyclerView.Adapter<AdapterCridet.myViewHolder>{
   List<CreditComputeDate> cardDetials;
    int S = 0;
    SimpleDateFormat dateformarter;
    Context This;
    public AdapterCridet(List<CreditComputeDate> cardDetials, Context This){
       this.cardDetials=cardDetials;
        this.This=This;
        dateformarter=new SimpleDateFormat("dd.MM.yyyy");
    }


    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
    final CreditComputeDate itemCr= cardDetials.get(position);
        holder.credit_procent.setText(parseToWithoutNull(itemCr.getProcent_100_system())+"%");
        holder.total_value.setText(parseToWithoutNull(itemCr.getTotal_value())+itemCr.getValyuta().getName());
        holder.total_paid.setText(parseToWithoutNull(itemCr.getTotal_paid())+itemCr.getValyuta().getName());

        Date AAa = (new Date());
        AAa.setTime(itemCr.getDate_start());
        holder.taken_credit_date.setText(dateformarter.format(AAa));

        long for_compute_interval=itemCr.getInterval();
        String left_date_string="";

        if (for_compute_interval/1000/60/60/24<1){
            holder.pay_or_archive.setText(R.string.archive);
            holder.left_date.setText(R.string.ends);
            holder.left_date.setTextColor(Color.parseColor(String.valueOf(R.color.red)));

        }
        else{
            int a=(int)for_compute_interval/1000/60/60/24/365;
            Log.d("valeee",""+a);
            if(a>=1){
                if(a>1){
                    left_date_string+=Integer.toString(a)+" "+This.getString(R.string.years);

                }
                else{
                    left_date_string+=Integer.toString(a)+" "+This.getString(R.string.year);
                }

                for_compute_interval-=a*1000*60*60*24*365;
            }
            int b=(int)for_compute_interval/1000/60/60/24/30;
            Log.d("valeee",""+b);
            if(b>=1){
                if(!left_date_string.matches("")){
                    left_date_string+=" ";
                }
                if(b>1){
                    left_date_string+=Integer.toString(b)+" "+This.getString(R.string.moths);

                }
                else{
                    left_date_string+=Integer.toString(b)+" "+This.getString(R.string.moth);
                }
                for_compute_interval-=a*1000*60*60*24*30;
            }
            int c=(int)for_compute_interval/1000/60/60/24;
            Log.d("valeee",""+c);
            if(c>=1){
                if(!left_date_string.matches("")){
                    left_date_string+=" ";
                }
                if(c>1){
                    left_date_string+=Integer.toString(c)+" "+This.getString(R.string.days);

                }
                else{
                    left_date_string+=Integer.toString(c)+" "+This.getString(R.string.day);
                }
                for_compute_interval-=a*1000*60*60*24;
            }
            holder.left_date.setText(left_date_string);

        }



        Log.d("interal",""+itemCr.getInterval());
        holder.overall_amount.setText(parseToWithoutNull(itemCr.getTotal_value()-itemCr.getTotal_paid())+itemCr.getValyuta().getName());

    }

    @Override
    public int getItemCount() {
        return cardDetials.size();
    }

    public String parseToWithoutNull(double A){
        if(A==(int)A)
            return Integer.toString((int)A);
        else
            return Double.toString(A);

    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moder_titem, parent, false);
        // set the view's size, margins, paddings and layout parameters

        myViewHolder vh = new myViewHolder(v);
        return vh;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView credit_procent;
        TextView total_value;
        TextView total_paid;
        TextView taken_credit_date;
        TextView left_date;
        TextView overall_amount;
        TextView pay_or_archive;
        View glav;
        public myViewHolder(View v) {
            super(v);
            credit_procent=(TextView) v.findViewById(R.id.procent_of_credit);
            total_value=(TextView) v.findViewById(R.id.total_value);
            total_paid=(TextView) v.findViewById(R.id.totalpayd);
            taken_credit_date=(TextView) v.findViewById(R.id.date_start);
            left_date=(TextView) v.findViewById(R.id.left_date);
            overall_amount=(TextView) v.findViewById(R.id.overallpay);
            pay_or_archive=(TextView) v.findViewById(R.id.pay);
            glav=v;
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
