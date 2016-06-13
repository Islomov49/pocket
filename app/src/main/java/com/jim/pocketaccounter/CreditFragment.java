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
import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.finance.Currency;
import com.melnykov.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class CreditFragment extends Fragment {
    ArrayList<CreditDetials> crList;
    ArrayList<CreditDetials> creditDetialsesList;
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
            public void togoInfo(CreditDetials current) {
                //lisdat positon turgan obyektni yuboramiz
                InfoCreditFragment temp=new InfoCreditFragment();
                temp.setConteent(current);
                openFragment(temp,"InfoFragment");

            }
        });
        crRV.setAdapter(crAdap);

        updateList();
        V.post(new Runnable() {
            @Override
            public void run() {


            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openFragment(new InfoCreditFragment(),"InfoFragment");
                openFragment(new AddCreditFragment(),"Addcredit");
            }
        });
        return V;
    }
    public void openFragment(Fragment fragment,String tag) {
        if (fragment != null) {
            if(tag.matches("Addcredit"))
            ((AddCreditFragment)fragment).addEventLis(new EventFromAdding() {
                @Override
                public void addedCredit() {
                    updateToFirst();
                }

                @Override
                public void canceledAdding() {
                    //some think when canceled
                }
            });
            final android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(tag).setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.add(R.id.flMain, fragment,tag);
            ft.commit();
        }
    }
    public void updateToFirst(){
        creditDetialsesList=PocketAccounter.financeManager.getCredits();
        crList.add(0,creditDetialsesList.get(0));
        crAdap.notifyItemInserted(0);
    }
    public void updateList(){
        crList.clear();
        creditDetialsesList=PocketAccounter.financeManager.getCredits();
        for (CreditDetials temp:creditDetialsesList){
            crList.add(temp);
        }
        crAdap.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    interface EventFromAdding{
        void addedCredit();
        void canceledAdding();
    }


}
