package com.jim.pocketaccounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.debts.AdapterCridet;
import com.jim.pocketaccounter.debts.CreditComputeDate;
import com.jim.pocketaccounter.debts.CreditDetials;
import com.melnykov.fab.FloatingActionButton;


import java.text.SimpleDateFormat;
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

        crAdap=new AdapterCridet(crList,This);
        crRV.setAdapter(crAdap);

        V.post(new Runnable() {
            @Override
            public void run() {
                crList.add(new CreditComputeDate(1,"Credit for Tv","$" ,120,1320, 3200,230,System.currentTimeMillis()));


                Date AAa = (new Date());

                crAdap.notifyItemInserted(0);

            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crList.add(0,new CreditComputeDate(1,"Master Card Credit","$" ,78,480, 580,0,System.currentTimeMillis()));

                    crAdap.notifyItemInserted(0);
                crRV.scrollToPosition(0);
            }
        });
        return V;
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
