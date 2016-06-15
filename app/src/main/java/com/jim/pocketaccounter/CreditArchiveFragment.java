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
        crList=PocketAccounter.financeManager.getArchiveCredits();
        crAdap=new AdapterCridetArchive(crList, This, new AdapterCridetArchive.forListnerArchive() {
            @Override
            public void togoInfo(CreditDetials current,int position) {
                InfoCreditFragmentForArchive temp=new InfoCreditFragmentForArchive();
                temp.setConteent(current, position, new ListnerDel() {
                    @Override
                    public void delete_item(int position) {
                        crList.remove(position);
                        PocketAccounter.financeManager.saveArchiveCredits();
                        crAdap.notifyItemRemoved(position);
                    }
                });
                openFragment(temp,"InfoFragment");
            }


            @Override
            public void item_delete(int position) {
                //TODO DELETE FROM DATABASE
                crList.remove(position);
                crAdap.notifyItemRemoved(position);
            }


        });

        crRV.setAdapter(crAdap);
        V.post(new Runnable() {
            @Override
            public void run() {

                updateList();
            }
        });


        return V;
    }

    public void openFragment(Fragment fragment,String tag) {
        if (fragment != null) {
            final android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(tag).setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.add(R.id.flMain, fragment,tag);
            ft.commit();
        }
    }
    public void updateList(){
        crList=PocketAccounter.financeManager.getArchiveCredits();
        crAdap.notifyDataSetChanged();
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
    interface ListnerDel{
        public void delete_item(int position);
    }

}
