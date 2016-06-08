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
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.IconAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.R.id.selectedIcon;


public class AddCreditFragment extends Fragment {
    TextInputLayout credNameinput;
    EditText credNameEdit;
    Spinner spiner_forValut,spiner_procent,spinner_peiod,spiner_trasnact;
    ImageView icona;
    String [] valyutes;
    String [] valyutes_symbols;
    String [] accs;
    int selectedIcon;
    ImageView ivToolbarMostRight;
    EditText nameCred,valueCred,procentCred,periodCred,firstCred ,lastCred,transactionCred;
    Context context;
    Animation wooble;
    SimpleDateFormat dateformarter;

    long forDay=1000L*60L*60L*24L;
    long forMoth=1000L*60L*60L*24L*30L;
    long forYear=1000L*60L*60L*24L*365L;

    public AddCreditFragment() {
        // Required empty public constructor

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
        spiner_forValut=(Spinner) V.findViewById(R.id.spinner);
        spiner_procent=(Spinner) V.findViewById(R.id.spinner_procent);
        spinner_peiod=(Spinner) V.findViewById(R.id.spinner_period);
        spiner_trasnact=(Spinner) V.findViewById(R.id.spinner_sceta);

        nameCred=(EditText) V.findViewById(R.id.editText) ;
        valueCred=(EditText) V.findViewById(R.id.value_credit) ;
        procentCred=(EditText) V.findViewById(R.id.procent_credit) ;
        periodCred=(EditText) V.findViewById(R.id.for_period_credit) ;
        firstCred=(EditText) V.findViewById(R.id.date_pick_edit) ;
        lastCred=(EditText) V.findViewById(R.id.date_ends_edit) ;
        transactionCred=(EditText) V.findViewById(R.id.for_trasaction_credit) ;

        wooble= AnimationUtils.loadAnimation(context,R.anim.wobble);

        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.check_sign);
        ivToolbarMostRight.setVisibility(View.VISIBLE);

        dateformarter=new SimpleDateFormat("dd.MM.yyyy");

        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //name checking
                 if (nameCred.getText().toString().equals("")){
                    nameCred.setError("Name should not empty!");
                    nameCred.startAnimation(wooble);

                 }
                else nameCred.setHintTextColor(ContextCompat.getColor(context,R.color.black_for_glavniy_text));

                //value cheking
                if (valueCred.getText().toString().equals("")){
                valueCred.setError("Value should not empty!");
                    valueCred.startAnimation(wooble);
                }
                else valueCred.setHintTextColor(ContextCompat.getColor(context,R.color.black_for_glavniy_text));


            }
        });

        final DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                Date AAa = (new Date());
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
        };

        lastCred.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar calendar = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            getDatesetListener2, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                }
            }
        });
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


        firstCred.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Calendar calendar = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            getDatesetListener, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                }
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



        icona=(ImageView) V.findViewById(R.id.imageForIcon) ;
        String[] tempIcons = getResources().getStringArray(R.array.icons);
        final int[] icons = new int[tempIcons.length];

        for (int i=0; i<tempIcons.length; i++)
            icons[i] = getResources().getIdentifier(tempIcons[i], "drawable", getActivity().getPackageName());
        selectedIcon = icons[18];

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
        ArrayList<Currency> currencies = manager.getCurrencies();
        valyutes = new String[currencies.size()];
        valyutes_symbols = new String[currencies.size()];

        for (int i = 0; i < valyutes.length; i++) {
            valyutes[i] = currencies.get(i).getName();
        }
        for (int i = 0; i < valyutes.length; i++) {
            valyutes_symbols[i] = currencies.get(i).getAbbr();
        }

        ArrayList<Account> accounts = manager.getAccounts();
        accs = new String[accounts.size()];

        for (int i = 0; i < accounts.size(); i++) {
            accs[i] = accounts.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_spiner,
                new String[] {
                        "Per year", "Per month", "Per day"
                } );



        ArrayAdapter<String> adapter_valyuta = new ArrayAdapter<String>(getActivity(),
               R.layout.adapter_spiner, valyutes );


        ArrayAdapter<String> adapter_period = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_spiner, new String[] {
                "Month", "Year", "Day"
        });

        ArrayAdapter<String> adapter_scet = new ArrayAdapter<String>(getActivity(),
                R.layout.adapter_spiner, accs);

        spiner_forValut.setAdapter(adapter_valyuta);
        spiner_procent.setAdapter(adapter);
        spinner_peiod.setAdapter(adapter_period);
        spiner_trasnact.setAdapter(adapter_scet);
        return  V;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ivToolbarMostRight.setVisibility(View.INVISIBLE);

    }


}
