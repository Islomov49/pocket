package com.jim.pocketaccounter;

import android.content.Context;
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
import com.jim.pocketaccounter.credit.ReckingCredit;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.melnykov.fab.FloatingActionButton;
import java.util.ArrayList;



public class CreditFragment extends Fragment {
    ArrayList<CreditDetials> crList;
    ArrayList<CreditDetials> creditDetialsesList;
    RecyclerView crRV;
    AdapterCridet crAdap;
    Context This;
    CreditTabLay.SvyazkaFragmentov svyaz;
    private FinanceManager financeManager;
    public CreditFragment() {
        // Required empty public constructor

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crList=PocketAccounter.financeManager.getCredits();
        This=getActivity();
    }
    public  CreditTabLay.ForFab getEvent(){
        return new CreditTabLay.ForFab() {
            @Override
            public void pressedFab() {
                openFragment(new AddCreditFragment(),AddCreditFragment.OPENED_TAG);
            }
        };
    }
    public void setSvyaz(CreditTabLay.SvyazkaFragmentov A){
        svyaz=A;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V=inflater.inflate(R.layout.fragment_credit, container, false);
        financeManager = PocketAccounter.financeManager;
        crRV=(RecyclerView) V.findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(This);
        crRV.setLayoutManager(llm);

        crAdap=new AdapterCridet(crList, This, new AdapterCridet.forListner() {
            @Override
            public void togoInfo(CreditDetials current,int position) {
                InfoCreditFragment temp=new InfoCreditFragment();
                temp.setConteent(current, position, new InfoCreditFragment.ConWithFragments() {
                    @Override
                    public void change_item(CreditDetials changed_item, int position) {
                        crList.set(position,changed_item);
                        crAdap.notifyItemChanged(position);
                    }

                    @Override
                    public void to_Archive(int position) {
                        CreditDetials toArc=crList.get(position);
                        if(toArc.isKey_for_include()){
                            toArc.setKey_for_archive(true);
                            crList.set(position,toArc);
                            crAdap.notifyItemChanged(position);
                        }
                        else{
                            crList.remove(position);
                            crAdap.notifyItemRemoved(position);
                        }

                        long toArchiveID=System.currentTimeMillis();
                        toArc.setMyCredit_id(toArchiveID);
                        ArrayList<ReckingCredit> recc=new ArrayList<>();
                        for(ReckingCredit mycr:toArc.getReckings()){
                            mycr.setMyCredit_id(toArchiveID);
                            recc.add(mycr);
                        }
                        toArc.setReckings(recc);
                        ArrayList<CreditDetials> A=financeManager.getArchiveCredits();
                        A.add(0,toArc);
                        financeManager.saveArchiveCredits();
                        svyaz.itemInsertedToArchive();
                    }
                    @Override
                    public void delete_item(int position) {
                        crList.remove(position);
                        financeManager.saveCredits();
                        crAdap.notifyItemRemoved(position);
                    }
                });
                openFragment(temp,"InfoFragment");
            }

            @Override
            public void change_item(CreditDetials current, int position) {
                crList.set(position,current);
                crAdap.notifyItemChanged(position);
            }

            @Override
            public void item_delete(int position) {
                crList.remove(position);
                crAdap.notifyItemRemoved(position);
            }

            @Override
            public void item_to_archive(int position) {
                //TODO to archive code
                CreditDetials toArc=crList.get(position);
                if(toArc.isKey_for_include()){
                    toArc.setKey_for_archive(true);
                    crList.set(position,toArc);
                    crAdap.notifyItemChanged(position);
                }
                else{
                    crList.remove(position);
                    crAdap.notifyItemRemoved(position);
                }

                long toArchiveID=System.currentTimeMillis();
                toArc.setMyCredit_id(toArchiveID);
                ArrayList<ReckingCredit> recc=new ArrayList<>();
                for(ReckingCredit mycr:toArc.getReckings()){
                    mycr.setMyCredit_id(toArchiveID);
                    recc.add(mycr);
                }
                toArc.setReckings(recc);
                financeManager.getArchiveCredits().add(0,toArc);
                financeManager.saveArchiveCredits();
                financeManager.saveCredits();
                svyaz.itemInsertedToArchive();
            }
        });

        V.post(new Runnable() {
            @Override
            public void run() {
                crRV.setAdapter(crAdap);

                updateList();

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
        // crList.add(0,creditDetialsesList.get(0));
        crList=PocketAccounter.financeManager.getCredits();
        crAdap.notifyItemInserted(0);
        // financeManager.setCredits(crList);
        crRV.scrollToPosition(0);
    }
    public void updateList(){
        //   crList.clear();
      /* creditDetialsesList=PocketAccounter.financeManager.getCredits();
        for (CreditDetials temp:creditDetialsesList){
            crList.add(temp);
        }*/
        crList=PocketAccounter.financeManager.getCredits();
        crAdap.notifyDataSetChanged();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onStop() {
        super.onStop();
        //  PocketAccounter.financeManager.setCredits(crList);
        PocketAccounter.financeManager.saveCredits();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        PocketAccounter.financeManager.saveCredits();
    }
    interface EventFromAdding{
        void addedCredit();
        void canceledAdding();
    }


}
