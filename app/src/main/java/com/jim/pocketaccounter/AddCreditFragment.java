package com.jim.pocketaccounter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class AddCreditFragment extends Fragment {
    TextInputLayout credNameinput;
    EditText credNameEdit;
    Spinner spiner_forValut,spiner_procent,spinner_peiod,spiner_trasnact;
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
        spiner_forValut=(Spinner) V.findViewById(R.id.spinner);
        spiner_procent=(Spinner) V.findViewById(R.id.spinner_procent);
        spinner_peiod=(Spinner) V.findViewById(R.id.spinner_period);
        spiner_trasnact=(Spinner) V.findViewById(R.id.spinner_sceta);

        V.findViewById(R.id.pustoyy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {
                "Dollar", "Rubl", "Sum"
        });


        ArrayAdapter<String> adapter_valyuta = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {
                "Per moth", "Per year", "Per day"
        });


        ArrayAdapter<String> adapter_period = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {
                "Moth", "Year", "Day"
        });

        ArrayAdapter<String> adapter_scet = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, new String[] {
                "Visa card", "Master card", "Bit coins"
        });

        spiner_forValut.setAdapter(adapter);
        spiner_procent.setAdapter(adapter_valyuta);
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
    }


}
