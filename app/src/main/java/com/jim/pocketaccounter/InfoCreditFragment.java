package com.jim.pocketaccounter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.pocketaccounter.credit.CreditDetials;

public class InfoCreditFragment extends Fragment {



    public InfoCreditFragment() {
        // Required empty public constructor
    }
    public void shareData(CreditDetials current){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_info_credit, container, false);
        V.findViewById(R.id.pustooyy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return V;
    }

    public void onButtonPressed(Uri uri) {

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
