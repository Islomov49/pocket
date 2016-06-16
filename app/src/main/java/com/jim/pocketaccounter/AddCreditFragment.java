package com.jim.pocketaccounter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.credit.ReckingCredit;
import com.jim.pocketaccounter.debt.Recking;
import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.IconAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.R.id.selectedIcon;


public class AddCreditFragment extends Fragment {
    boolean onSucsessed=false;
    Spinner spiner_forValut,spiner_procent,spinner_peiod,spiner_trasnact;
    ImageView icona;
    String [] valyutes;
    String [] valyutes_symbols;
    String [] accs;
    ArrayList<Account> accounts;
    int selectedIcon;
    ImageView ivToolbarMostRight;
    EditText nameCred,valueCred,procentCred,periodCred,firstCred ,lastCred,transactionCred;
    Context context;
    SimpleDateFormat dateformarter;
    int argFirst[]=new int[3];
    int argLast[]=new int[3];
    long forDay=1000L*60L*60L*24L;
    long forMoth=1000L*60L*60L*24L*30L;
    long forWeek=1000L*60L*60L*24L*7L;
    long forYear=1000L*60L*60L*24L*365L;
    boolean isAv=true;
    ArrayList<Currency> currencies;
    int isAvInt=0;
    CreditFragment.EventFromAdding eventLis;
    AddCreditFragment ThisFragment;
    ArrayList<CreditDetials> myList;
    CheckBox isOpkey;
    public static final String OPENED_TAG="Addcredit";
    public static  boolean to_open_dialog=false;
    private FinanceManager manager;

    public AddCreditFragment() {
        // Required empty public constructor
        ThisFragment=this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V=inflater.inflate(R.layout.fragment_add_credit, container, false);
        context=getActivity();
     //   to_open_dialog=false;
        Log.d("args",argFirst[0]+argFirst[1]+argFirst[2]+"");
        spiner_forValut=(Spinner) V.findViewById(R.id.spinner);
        spiner_procent=(Spinner) V.findViewById(R.id.spinner_procent);
        spinner_peiod=(Spinner) V.findViewById(R.id.spinner_period);
        spiner_trasnact=(Spinner) V.findViewById(R.id.spinner_sceta);
        isOpkey=(CheckBox) V.findViewById(R.id.key_for_balance);
        to_open_dialog=false;
        nameCred=(EditText) V.findViewById(R.id.editText) ;
        valueCred=(EditText) V.findViewById(R.id.value_credit) ;
        procentCred=(EditText) V.findViewById(R.id.procent_credit) ;
        periodCred=(EditText) V.findViewById(R.id.for_period_credit) ;
        firstCred=(EditText) V.findViewById(R.id.date_pick_edit) ;
        lastCred=(EditText) V.findViewById(R.id.date_ends_edit) ;
        transactionCred=(EditText) V.findViewById(R.id.for_trasaction_credit) ;

        nameCred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(nameCred.getText().toString().matches("")){
                    to_open_dialog=false;
                }
                else{

                        to_open_dialog=true;

                }
                Log.d("somesome",""+to_open_dialog+" "+nameCred.getText().toString());
            }
        });

        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.check_sign);
        ivToolbarMostRight.setVisibility(View.VISIBLE);
        dateformarter=new SimpleDateFormat("dd.MM.yyyy");
        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //name checking
                boolean isMojno=true;
                 if (nameCred.getText().toString().equals("")){
                    nameCred.setError("Name should not empty!");
                     isMojno=false;
                 }
                else nameCred.setHintTextColor(ContextCompat.getColor(context,R.color.black_for_secondary_text));

                //value cheking
                if (valueCred.getText().toString().equals("")){
                valueCred.setError("Value should not empty!");
                    isMojno=false;
                }

                if (procentCred.getText().toString().equals("")){
                    procentCred.setError("Procent should not empty!");
                    isMojno=false;
                }

                if (periodCred.getText().toString().equals("")){
                    periodCred.setError("Procent should not empty!");
                    isMojno=false;
                }
                if (firstCred.getText().toString().equals("")){
                    firstCred.setError("After Period choise geting or last return date!");
                    isMojno=false;
                }

               //TODO first transaction


                if(isMojno){

                   openDialog();
                }


            }
        });

        final DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                Date AAa = (new Date());
                argFirst[0]=arg1;
                argFirst[1]=arg2;
                argFirst[2]=arg3;
                Calendar calend=new GregorianCalendar(arg1,arg2,arg3);
                AAa.setTime(calend.getTimeInMillis());

                firstCred.setText(dateformarter.format(AAa));

                int period_long=1;
                if(!periodCred.getText().toString().matches("")){
                    period_long=Integer.parseInt(periodCred.getText().toString());
                    switch (spinner_peiod.getSelectedItemPosition()){
                        case 0:
                            //moth
                            calend.add(Calendar.MONTH, period_long);

                            break;
                        case 1:
                            //year
                            calend.add(Calendar.YEAR, period_long);
                            break;
                        case 2:
                            //week
                            calend.add(Calendar.WEEK_OF_YEAR, period_long);
                            break;
                        case 3:
                            //day
                            calend.add(Calendar.DAY_OF_YEAR, period_long);

                            break;
                        default:
                            break;
                    }


                    long forCompute=calend.getTimeInMillis();
                    // forCompute+=period_long;

                    AAa.setTime(forCompute);
                    lastCred.setText(dateformarter.format(AAa));

                }
                else {
                    periodCred.setError("First enter period of debt!");
                }
            }
        };
        final DatePickerDialog.OnDateSetListener getDatesetListener2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                argLast[0]=arg1;
                argLast[1]=arg2;
                argLast[2]=arg3;

                Date AAa = (new Date());
                Calendar calend=new GregorianCalendar(arg1,arg2,arg3);
                AAa.setTime(calend.getTimeInMillis());


                lastCred.setText(dateformarter.format(AAa));


                int period_long=1;
                if(!periodCred.getText().toString().matches("")){
                    period_long=Integer.parseInt(periodCred.getText().toString());
                    switch (spinner_peiod.getSelectedItemPosition()){
                        case 0:
                            //moth
                            calend.add(Calendar.MONTH, -period_long);

                            break;
                        case 1:
                            //year
                            calend.add(Calendar.YEAR, -period_long);
                            break;
                        case 2:
                            //week
                            calend.add(Calendar.WEEK_OF_YEAR, -period_long);
                            break;
                        case 3:
                            //day
                            calend.add(Calendar.DAY_OF_YEAR, -period_long);

                            break;
                        default:
                            break;
                    }


                    long forCompute=calend.getTimeInMillis();

                    AAa.setTime(forCompute);
                    firstCred.setText(dateformarter.format(AAa));

                }
                else {
                    periodCred.setError("First enter period of debt!");
                }

            }
        };


        lastCred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        getDatesetListener2, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
        firstCred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        getDatesetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });


        isOpkey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isOpkey.isChecked()){
                    spiner_trasnact.setVisibility(View.VISIBLE);
                }
                else spiner_trasnact.setVisibility(View.GONE);
            }
        });
        procentCred.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String A=procentCred.getText().toString();
                    Log.d("someeV",isAvInt+" "+A.length());
                    if(!A.equals("")) {
                        //ADDING SUFFIX AGAIN
                        if(A.contains("%")){
                            StringBuilder sb = new StringBuilder(A);
                            sb.deleteCharAt(A.indexOf("%"));
                            procentCred.setText(sb.toString()+"%");

                        }
                        else {
                            procentCred.setText(A+"%");

                        }


                    }
                }
            }
        });
        spinner_peiod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(argFirst[0]!=0){
                    forDateSyncFirst();
                }
                else if(argLast[0]!=0){
                    forDateSyncLast();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        periodCred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(argFirst[0]!=0){
                    forDateSyncFirst();
                }
                else if(argLast[0]!=0){
                    forDateSyncLast();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        icona=(ImageView) V.findViewById(R.id.imageForIcon) ;
        String[] tempIcons = getResources().getStringArray(R.array.icons);
        final int[] icons = new int[tempIcons.length];

        for (int i=0; i<tempIcons.length; i++)
            icons[i] = getResources().getIdentifier(tempIcons[i], "drawable", getActivity().getPackageName());
        selectedIcon = icons[0];

        icona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.cat_icon_select, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                GridView gvIcon = (GridView) dialogView.findViewById(R.id.gvCategoryIcons);
                IconAdapter adapter = new IconAdapter(getActivity(), icons, selectedIcon);
                gvIcon.setAdapter(adapter);
                gvIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bitmap temp = BitmapFactory.decodeResource(getResources(), icons[position]);
                        Bitmap icon = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
                        icona.setImageBitmap(icon);
                        selectedIcon = icons[position];
                        dialog.dismiss();
                    }
                });
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                dialog.getWindow().setLayout(7*width/8, RelativeLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        });


        V.findViewById(R.id.pustoyy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FinanceManager manager = new FinanceManager(getContext());
        currencies = manager.getCurrencies();
        valyutes = new String[currencies.size()];
        valyutes_symbols = new String[currencies.size()];

        for (int i = 0; i < valyutes.length; i++) {
            valyutes[i] = currencies.get(i).getName();
        }
        for (int i = 0; i < valyutes.length; i++) {
            valyutes_symbols[i] = currencies.get(i).getAbbr();
        }

       accounts = manager.getAccounts();
        accs = new String[accounts.size()];
        for (int i = 0; i < accounts.size(); i++) {
            accs[i] = accounts.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_spiner,
                new String[] {
                        getString(R.string.per_month), getString(R.string.per_year),getString(R.string.per_week), getString(R.string.per_day)
                } );



        ArrayAdapter<String> adapter_valyuta = new ArrayAdapter<String>(getActivity(),
               R.layout.adapter_spiner, valyutes );


        ArrayAdapter<String> adapter_period = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_spiner, new String[] {
                getString(R.string.mont), getString(R.string.yearr), getString(R.string.weekk),getString(R.string.dayy)
        });

        ArrayAdapter<String> adapter_scet = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_gravity_right, accs);

        spiner_forValut.setAdapter(adapter_valyuta);
        spiner_procent.setAdapter(adapter);
        spinner_peiod.setAdapter(adapter_period);
        spiner_trasnact.setAdapter(adapter_scet);
        return  V;
    }

    public void forDateSyncFirst(){
        Date AAa = (new Date());
        Calendar calend=new GregorianCalendar(argFirst[0],argFirst[1],argFirst[2]);
        AAa.setTime(calend.getTimeInMillis());

        firstCred.setText(dateformarter.format(AAa));


        int period_long=1;
        if(!periodCred.getText().toString().matches("")){
            period_long=Integer.parseInt(periodCred.getText().toString());
            switch (spinner_peiod.getSelectedItemPosition()){
                case 0:
                    //moth
                    calend.add(Calendar.MONTH, period_long);

                    break;
                case 1:
                    //year
                    calend.add(Calendar.YEAR, period_long);
                    break;
                case 2:
                    //week
                    calend.add(Calendar.WEEK_OF_YEAR, period_long);
                    break;
                case 3:
                    //day
                    calend.add(Calendar.DAY_OF_YEAR, period_long);

                    break;
                default:
                    break;
            }


            long forCompute=calend.getTimeInMillis();
            // forCompute+=period_long;

            AAa.setTime(forCompute);
            lastCred.setText(dateformarter.format(AAa));

        }
        else {
            periodCred.setError("First enter period of debt!");
        }
    }

    public void forDateSyncLast(){
        Date AAa = (new Date());
        Calendar calend=new GregorianCalendar(argLast[0],argLast[1],argLast[2]);
        AAa.setTime(calend.getTimeInMillis());

        lastCred.setText(dateformarter.format(AAa));


        int period_long=1;
        if(!periodCred.getText().toString().matches("")){
            period_long=Integer.parseInt(periodCred.getText().toString());
            switch (spinner_peiod.getSelectedItemPosition()){
                case 0:
                    //moth
                    calend.add(Calendar.MONTH, -period_long);

                    break;
                case 1:
                    //year
                    calend.add(Calendar.YEAR, -period_long);
                    break;
                case 2:
                    //week
                    calend.add(Calendar.WEEK_OF_YEAR, -period_long);
                    break;
                case 3:
                    //day
                    calend.add(Calendar.DAY_OF_YEAR, -period_long);

                    break;
                default:
                    break;
            }


            long forCompute=calend.getTimeInMillis();
            // forCompute+=period_long;

            AAa.setTime(forCompute);
            firstCred.setText(dateformarter.format(AAa));

        }
        else {
            periodCred.setError("First enter period of debt!");
        }
    }
    StringBuilder sb;
    double creditValueWith;
    private void openDialog () {
        final Dialog dialog=new Dialog(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.info_about_all, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        manager = PocketAccounter.financeManager;

        final TextView value = (TextView) dialogView.findViewById(R.id.textView9);
        final TextView procent = (TextView) dialogView.findViewById(R.id.textView11);
        final EditText solution = (EditText) dialogView.findViewById(R.id.edit_result);
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowCancel);
        ImageView save = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowSave);
        sb=new StringBuilder(procentCred.getText().toString());
        int a=sb.toString().indexOf('%');
        if(a!=-1)
            sb.deleteCharAt(a);
        value.setText(valueCred.getText().toString());
        procent.setText(procentCred.getText().toString());
        solution.setText(parseToWithoutNull(Double.parseDouble(valueCred.getText().toString())*(1d+Double.parseDouble(sb.toString())/100)));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long procent_inter=1;
                switch (spiner_procent.getSelectedItemPosition()){
                    case 0:
                        procent_inter*=forMoth;
                        break;
                    case 1:
                        procent_inter*=forYear;
                        break;
                    case 2:
                        procent_inter*=forWeek;
                        break;
                    case 3:
                        procent_inter*=forDay;
                        break;
                }
                long period_inter=Long.parseLong(periodCred.getText().toString());
                long period_tip = 0;
                switch (spinner_peiod.getSelectedItemPosition()){
                    case 0:
                        period_inter*=forMoth;
                        period_tip=forMoth;
                        break;
                    case 1:
                        period_inter*=forYear;
                        period_tip=forYear;
                        break;
                    case 2:
                        period_inter*=forWeek;
                        period_tip=forWeek;
                        break;
                    case 3:
                        period_inter*=forDay;
                        period_tip=forDay;
                        break;
                }
                myList=manager.getCredits();

                boolean key=true;
                key = isOpkey.isChecked();

                String sloution = solution.getText().toString();
                sloution.replace(',', '.');

                    CreditDetials A1 = new CreditDetials(selectedIcon, nameCred.getText().toString(), new GregorianCalendar(argFirst[0], argFirst[1], argFirst[2]),
                            Double.parseDouble(sb.toString()), procent_inter, period_inter, period_tip, key, Double.parseDouble(valueCred.getText().toString()),
                            currencies.get(spiner_forValut.getSelectedItemPosition()), Double.parseDouble(sloution), System.currentTimeMillis());

                    String transactionCredString=transactionCred.getText().toString();
                if(!transactionCredString.matches("")){
                    ReckingCredit first_pay;
                    if(key&&accounts.size()!=0) {
                      first_pay = new ReckingCredit((new GregorianCalendar(argFirst[0], argFirst[1], argFirst[2])).getTimeInMillis(), Double.parseDouble(transactionCredString), accounts.get(spiner_trasnact.getSelectedItemPosition()).getId(),
                                A1.getMyCredit_id(),    getString(R.string.this_first_comment));
                    }
                    else {
                        first_pay = new ReckingCredit((new GregorianCalendar(argFirst[0], argFirst[1], argFirst[2])).getTimeInMillis(), Double.parseDouble(transactionCredString),"pustoy",
                                A1.getMyCredit_id(), getString(R.string.this_first_comment));

                    }
                    ArrayList<ReckingCredit> tempik=A1.getReckings();
                    tempik.add(0,first_pay);
                    A1.setReckings(tempik);
                }
                myList.add(0,A1);

                Log.d("soemeV",
                        A1.getCredit_name()+"\n"
                                +A1.getIcon_ID()+"\n"
                                +A1.getPeriod_time()+"\n"
                                +A1.getProcent()+"\n"+
                                A1.getProcent_interval()+"\n"+
                                A1.getTake_time().getTimeInMillis()+
                                "\n"+A1.getValue_of_credit()+"\n"+
                                A1.getValyute_currency().getName()+
                                "\n"+A1.getValue_of_credit_with_procent());
                dialog.dismiss();
                closeCurrentFragment();
                onSucsessed=true;
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(7*width/8, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void addEventLis(CreditFragment.EventFromAdding even){
        eventLis=even;
    }
    public String parseToWithoutNull(double A){
        if(A==(int)A)
            return Integer.toString((int)A);
        else{
            DecimalFormat format=new DecimalFormat("0.##");
            return format.format(A);
        }

    }
    @Override
    public void onStop(){
        super.onStop();
        if(onSucsessed){

        }


    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("Detached","true");
        ivToolbarMostRight.setVisibility(View.INVISIBLE);
        if(!onSucsessed)
        eventLis.canceledAdding();
        else{
            PocketAccounter.financeManager.saveCredits();
            eventLis.addedCredit();
        }

        super.onDetach();
    }




    public void closeCurrentFragment(){
        getActivity().getSupportFragmentManager().popBackStack ();
    }


}
