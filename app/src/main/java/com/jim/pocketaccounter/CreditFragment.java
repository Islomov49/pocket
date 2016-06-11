package com.jim.pocketaccounter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.credit.AdapterCridet;
import com.jim.pocketaccounter.credit.CreditComputeDate;
import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.finance.Currency;
import com.melnykov.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class CreditFragment extends Fragment {
    List<CreditComputeDate> crList;
    RecyclerView crRV;
    AdapterCridet crAdap;
    Context This;
    FloatingActionButton fb;
    public CreditFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crList=new ArrayList<>();
        This=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V=inflater.inflate(R.layout.fragment_credit, container, false);
        crRV=(RecyclerView) V.findViewById(R.id.my_recycler_view);
        fb=(FloatingActionButton) V.findViewById(R.id.fab);

        LinearLayoutManager llm = new LinearLayoutManager(This);
        crRV.setLayoutManager(llm);

        crAdap=new AdapterCridet(crList, This, new AdapterCridet.forListner() {
            @Override
            public void togoInfo(CreditComputeDate current) {
                //lisdat positon turgan obyektni yuboramiz
                Log.d("prostoo",""+current.getName());
                openFragment(new InfoCreditFragment(),"Addcredit");

            }
        });
        crRV.setAdapter(crAdap);

        crList.add(CompyuteData(new CreditDetials(R.drawable.ic_category_4,"Come Text",new GregorianCalendar(2016,06,06),
                20d ,1000L*60L*60L*24L*30L ,1000L*60L*60L*24L*33,8000,
                new Currency("\u20BD"),9000,System.currentTimeMillis() )));

        crList.add(new CreditComputeDate(R.drawable.ic_category_9,"Mobile credit",15,8000,3820,System.currentTimeMillis()-1000L*60L*60L*24L*100L,1000L*60L*60L*24L-1 , new Currency("\u20BD")));

        crAdap.notifyDataSetChanged();
        V.post(new Runnable() {
            @Override
            public void run() {


            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFragment(new AddCreditFragment(),"Addcredit");


            }
        });
        return V;
    }
    public void openFragment(Fragment fragment,String tag) {
        if (fragment != null) {
            final android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(tag);
            ft.add(R.id.flMain, fragment,tag);

            ft.commit();
        }
    }
    public CreditComputeDate CompyuteData(CreditDetials ItemDet){
        CreditComputeDate itemC=new CreditComputeDate();
        itemC.setID(ItemDet.getIcon_ID());
        itemC.setDate_start(ItemDet.getTake_time().getTimeInMillis());
        itemC.setInterval(ItemDet.getPeriod_time());
        itemC.setName(ItemDet.getCredit_name());
        itemC.setProcent_100_system(ItemDet.getProcent());

        //TODO Total paid computing
        double total_paid=100;

        itemC.setTotal_paid(total_paid);
        itemC.setValyuta(ItemDet.getValyute_currency());
        itemC.setTotal_value(ItemDet.getValue_of_credit_with_procent());
        return itemC;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
