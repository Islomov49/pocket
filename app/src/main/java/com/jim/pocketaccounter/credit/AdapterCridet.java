package com.jim.pocketaccounter.credit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.AddCreditFragment;
import com.jim.pocketaccounter.InfoCreditFragment;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by developer on 02.06.2016.
 */

public class AdapterCridet extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
   List<CreditDetials> cardDetials;
   List<CreditDetials> allDetials;
    int S = 0;
    SimpleDateFormat dateformarter;
    Context context;
    forListner A1;
    ArrayList<Account> accaunt_AC;
    boolean toArcive=false;
    long forDay=1000L*60L*60L*24L;
    long forMoth=1000L*60L*60L*24L*30L;
    long forYear=1000L*60L*60L*24L*365L;
    final static long forWeek=1000L*60L*60L*24L*7L;

    public AdapterCridet(List<CreditDetials> cardDetials, Context This, forListner A1){
        this.cardDetials=cardDetials;
        this.context=This;
        dateformarter=new SimpleDateFormat("dd.MM.yyyy");
        this.A1=A1;
    }

    public interface forListner{
         void togoInfo(CreditDetials current,int position);
         void change_item(CreditDetials current,int position);
         void item_delete(int position);
         void item_to_archive(int position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holdeer, final int position) {
        if(holdeer instanceof Fornull){
            return;
        }
        final myViewHolder holder=(myViewHolder) holdeer;

        final CreditDetials itemCr= cardDetials.get(position);
            holder.credit_procent.setText(parseToWithoutNull(itemCr.getProcent())+"%");
        holder.total_value.setText(parseToWithoutNull(itemCr.getValue_of_credit_with_procent())+itemCr.getValyute_currency().getAbbr());

        double total_paid=0;
        for(ReckingCredit item:itemCr.getReckings())
            total_paid+=item.getAmount();

        holder.total_paid.setText(parseToWithoutNull(total_paid)+itemCr.getValyute_currency().getAbbr());
        holder.nameCredit.setText(itemCr.getCredit_name());

        Date AAa = (new Date());
        AAa.setTime(itemCr.getTake_time().getTimeInMillis());
        holder.taken_credit_date.setText(dateformarter.format(AAa));
        holder.iconn.setImageResource(itemCr.getIcon_ID());

        Calendar to= (Calendar) itemCr.getTake_time().clone();
        long period_tip=itemCr.getPeriod_time_tip();
        long period_voqt=itemCr.getPeriod_time();
        int voqt_soni= (int) (period_voqt/period_tip);

        if(period_tip==forDay){
            to.add(Calendar.DAY_OF_YEAR, (int) voqt_soni);
        }
        else if(period_tip==forWeek){
            to.add(Calendar.WEEK_OF_YEAR, (int) voqt_soni);
        }
        else if(period_tip==forMoth){
            to.add(Calendar.MONTH, (int) voqt_soni);

        }
        else {
            to.add(Calendar.YEAR, (int) voqt_soni);

        }

        Date from=new Date();
        int t[]=getDateDifferenceInDDMMYYYY(from,to.getTime());
        Log.d("Myday",t[0]+" "+t[1]+" "+t[2]);
        if(t[0]*t[1]*t[2]<0&&(t[0]+t[1]+t[2])!=0){
            holder.left_date.setText(R.string.ends);
            holder.left_date.setTextColor(Color.parseColor("#832e1c"));
        }
        else {
            String left_date_string="";
            if(t[0]!=0){
                if(t[0]>1){
                    left_date_string+=Integer.toString(t[0])+" "+context.getString(R.string.years);
                }
                else{
                    left_date_string+=Integer.toString(t[0])+" "+context.getString(R.string.year);
                }

            }
            if(t[1]!=0){
                if(!left_date_string.matches("")){
                    left_date_string+=" ";
                }
                if(t[1]>1){
                    left_date_string+=Integer.toString(t[1])+" "+context.getString(R.string.moths);
                }
                else{
                    left_date_string+=Integer.toString(t[1])+" "+context.getString(R.string.moth);
                }
            }
            if(t[2]!=0){
                if(!left_date_string.matches("")){
                    left_date_string+=" ";
                }
                if(t[2]>1){
                    left_date_string+=Integer.toString(t[2])+" "+context.getString(R.string.days);

                }
                else{
                    left_date_string+=Integer.toString(t[2])+" "+context.getString(R.string.day);
                }
            }
            holder.left_date.setText(left_date_string);
        }
        if(itemCr.getValue_of_credit_with_procent()-total_paid<=0){

            holder.overall_amount.setText(context.getString(R.string.repaid));
            holder.pay_or_archive.setText(R.string.archive);

            toArcive=true;
        }
        else {
            toArcive=false;
            holder.overall_amount.setText(parseToWithoutNull(itemCr.getValue_of_credit_with_procent()-total_paid)+itemCr.getValyute_currency().getAbbr());
        } holder.glav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               A1.togoInfo(itemCr,position);
            }
        });
        holder.pay_or_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(toArcive)
              {
                A1.item_to_archive(position);
              }
                else
                openDialog(itemCr,position);
            }
        });
    }
    public static int [] getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate=Calendar.getInstance();
        Calendar toDate=Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year,month,day;
       if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment =fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
         return   new int[]{year, month, day};
    }

    @Override
    public int getItemCount() {
        return cardDetials.size();
    }
    final static int VIEW_NULL=0;
    final static int VIEW_NOT_NULL=1;

    @Override
    public int getItemViewType(int position) {
        return cardDetials.get(position).isKey_for_archive() ? VIEW_NULL : VIEW_NOT_NULL;
    }

    public String parseToWithoutNull(double A){
        if(A==(int)A)
            return Integer.toString((int)A);
        else
            return Double.toString(A);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_NULL) {
            View v=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.null_lay, parent, false);;
            vh=new Fornull(v);

        }
        else if(viewType==VIEW_NOT_NULL){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.moder_titem, parent, false);
            vh=new myViewHolder(v);
        }
        // set the view's size, margins, paddings and layout parameters


        return vh;
    }
    public static class Fornull extends RecyclerView.ViewHolder {

        public Fornull(View v) {
            super(v);

        }}

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView credit_procent;
        TextView total_value;
        TextView total_paid;
        TextView taken_credit_date;
        TextView left_date;
        TextView overall_amount;
        TextView pay_or_archive;
        TextView nameCredit;
        View glav;
        ImageView iconn;
        public myViewHolder(View v) {
            super(v);
            credit_procent=(TextView) v.findViewById(R.id.procent_of_credit);
            total_value=(TextView) v.findViewById(R.id.total_value);
            total_paid=(TextView) v.findViewById(R.id.totalpayd);
            taken_credit_date=(TextView) v.findViewById(R.id.date_start);
            left_date=(TextView) v.findViewById(R.id.left_date);
            overall_amount=(TextView) v.findViewById(R.id.overallpay);
            pay_or_archive=(TextView) v.findViewById(R.id.pay);
            nameCredit=(TextView) v.findViewById(R.id.NameCr);
            iconn=(ImageView) v.findViewById(R.id.iconaaa);
            glav=v;
        }

    }

    private void openDialog(final CreditDetials current, final int position) {
        final Dialog dialog = new Dialog(context);
        View dialogView = ((PocketAccounter)context).getLayoutInflater().inflate(R.layout.add_pay_debt_borrow_info_mod, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        final EditText enterDate = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowDate);
        final EditText enterPay = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPaySumm);
        final EditText comment = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPayComment);
        final Spinner accountSp = (Spinner) dialogView.findViewById(R.id.spInfoDebtBorrowAccount);
        if(current.isKey_for_include()){
            accaunt_AC=PocketAccounter.financeManager.getAccounts();
            String[] accaounts = new String[accaunt_AC.size()];
            for (int i = 0; i < accaounts.length; i++) {
                accaounts[i] = accaunt_AC.get(i).getName();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    context, R.layout.spiner_gravity_right, accaounts);

            accountSp.setAdapter(arrayAdapter);

        }
        else{
            dialogView.findViewById(R.id.is_calc).setVisibility(View.GONE);
        }
        final Calendar date = Calendar.getInstance();
        enterDate.setText(dateformarter.format(date.getTime()));
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowCancel);
        ImageView save = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowSave);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {

                enterDate.setText(dateformarter.format((new GregorianCalendar(arg1,arg2,arg3)).getTime()));
                date.set(arg1, arg2, arg3);
            }
        };
        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(context,
                        getDatesetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amount=enterPay.getText().toString();
                double total_paid=0;
                for(ReckingCredit item:current.getReckings())
                    total_paid+=item.getAmount();
                if(!amount.matches("")){
                    if(Double.parseDouble(amount)>current.getValue_of_credit_with_procent()-total_paid){

                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Your payment balance is "+parseToWithoutNull(current.getValue_of_credit_with_procent()-total_paid)+
                                current.getValyute_currency().getAbbr()+"."+" Are you sure of the fact that you have to pay more than "+
                                parseToWithoutNull(Double.parseDouble(amount)-(current.getValue_of_credit_with_procent()-total_paid))+
                                current.getValyute_currency().getAbbr()+" ?")
                                .setPositiveButton("I'm sure", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoge, int id) {
                                       String amount=enterPay.getText().toString();
                                        ReckingCredit rec=null;
                                        if(!amount.matches("")&&current.isKey_for_include())
                                        rec=new ReckingCredit(date.getTimeInMillis(),Double.parseDouble(amount),accaunt_AC.get(accountSp.getSelectedItemPosition()).getId(),current.getMyCredit_id(),comment.getText().toString());
                                        else
                                        rec=new ReckingCredit(date.getTimeInMillis(),Double.parseDouble(amount),"",current.getMyCredit_id(),comment.getText().toString());
                                        current.getReckings().add(rec);
                                        A1.change_item(current,position);
                                        dialog.dismiss();
                                                            }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });
                        builder.create().show();

                    }
                    else {
                        ReckingCredit rec=null;
                        if(!amount.matches("")&&current.isKey_for_include())
                            rec=new ReckingCredit(date.getTimeInMillis(),Double.parseDouble(amount),accaunt_AC.get(accountSp.getSelectedItemPosition()).getId(),current.getMyCredit_id(),comment.getText().toString());
                        else
                            rec=new ReckingCredit(date.getTimeInMillis(),Double.parseDouble(amount),"",current.getMyCredit_id(),comment.getText().toString());
                        current.getReckings().add(rec);
                        A1.change_item(current,position);
                        dialog.dismiss();
                    }
                }

            }
        });
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(7 * width / 8, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
