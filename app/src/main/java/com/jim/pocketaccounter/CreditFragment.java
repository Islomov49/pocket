package com.jim.pocketaccounter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.credit.AdapterCridet;
import com.jim.pocketaccounter.credit.CreditComputeDate;
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

        crAdap=new AdapterCridet(crList,This);
        crRV.setAdapter(crAdap);

        V.post(new Runnable() {
            @Override
            public void run() {
                crList.add(new CreditComputeDate(0,10,10000,1520,System.currentTimeMillis()-(long)1000*60*60*24*256,(long)1000*60*60*24*30, new Currency("$")));
                crAdap.notifyItemInserted(0);
/*

                Date AAa = (new Date());

                crAdap.notifyItemInserted(0);
*/
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
