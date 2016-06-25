package com.jim.pocketaccounter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.credit.AdapterCridet;
import com.jim.pocketaccounter.credit.AdapterCridetArchive;
import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.util.ArrayList;


public class CreditArchiveFragment extends Fragment {
    ArrayList<CreditDetials> crList;
    ArrayList<CreditDetials> creditDetialsesList;
    RecyclerView crRV;
    AdapterCridetArchive crAdap;
    Context This;
    AdapterCridetArchive.GoCredFragForNotify svyazForNotifyFromArchAdap;
    public void setSvyazToAdapter(AdapterCridetArchive.GoCredFragForNotify goNotify){
        svyazForNotifyFromArchAdap=goNotify;
    }
    public CreditArchiveFragment() {
        // Required empty public constructor

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         This=getActivity();
    }
    public CreditTabLay.SvyazkaFragmentov getSvyaz(){
        return new CreditTabLay.SvyazkaFragmentov() {
            @Override
            public void itemInsertedToArchive() {
                updateList();
            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V=inflater.inflate(R.layout.fragment_credit, container, false);
        crRV=(RecyclerView) V.findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(This);
        crRV.setLayoutManager(llm);
       // crList=PocketAccounter.financeManager.getArchiveCredits();

        crAdap=new AdapterCridetArchive( This);
        crAdap.setSvyazToAdapter(svyazForNotifyFromArchAdap);
        crRV.setAdapter(crAdap);


        return V;
    }

    public void updateList(){
        crAdap.notifyItemInserted(0);
        if(crRV.getChildCount()>0);
        crRV.scrollToPosition(0);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onStop() {
        super.onStop();
        PocketAccounter.financeManager.saveArchiveCredits();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        PocketAccounter.financeManager.saveArchiveCredits();
    }


}
