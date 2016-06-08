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
        crList.add(new CreditComputeDate(0,"Credit for TV", 10,10000,1520,System.currentTimeMillis()-1000L*60L*60L*24L*256L,1000L*60L*60L*24L*850L , new Currency("$")));
        crList.add(new CreditComputeDate(0,"Mobile credit",15,8000,3820,System.currentTimeMillis()-1000L*60L*60L*24L*100L,1000L*60L*60L*24L-1 , new Currency("\u20BD")));

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
