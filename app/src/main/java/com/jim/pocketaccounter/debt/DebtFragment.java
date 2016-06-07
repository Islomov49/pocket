package com.jim.pocketaccounter.debt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 6/4/2016.
 */

public class DebtFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter myAdapter;
    private FinanceManager financeManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeManager = new FinanceManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.borrow_fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lvBorrowFragment);

        ArrayList<DebtBorrow> borrowPersons = new ArrayList<>();
        borrowPersons.add(
                new DebtBorrow(new Person("Nasimxon", "+99898121225", ""),
                        Calendar.getInstance(),
                        Calendar.getInstance(),
                        false,
                        new Account(),
                        new Currency("asda"),
                        200.23,
                        false
                ));
        borrowPersons.add(
                new DebtBorrow(new Person("Barton", "+99898121225", ""),
                        Calendar.getInstance(),
                        Calendar.getInstance(),
                        false,
                        new Account(),
                        new Currency("asda"),
                        200.23,
                        false
                ));
        borrowPersons.add(
                new DebtBorrow(new Person("Bobur", "+99898121225", ""),
                        Calendar.getInstance(),
                        Calendar.getInstance(),
                        false,
                        new Account(),
                        new Currency("asda"),
                        200.23,
                        false
                ));
        financeManager.setDebtBorrows(borrowPersons);
        myAdapter = new MyAdapter(borrowPersons);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myAdapter);
        registerForContextMenu(recyclerView);

        return view;
    }

    public void addList() {
//        Person person = new Person("BARTON MARTON", "+9989255151", "200", "Data zayma: 05.06.2016", "Data vozvrata: 02.03.2016", "", "");
//        myAdapter.setDataChanged(person);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<DebtBorrow> persons;

        public MyAdapter(List<DebtBorrow> contactList) {
            persons = contactList;
        }

        public int getItemCount() {
            return persons.size();
        }

        public void onBindViewHolder(DebtFragment.ViewHolder view, final int position) {
            DebtBorrow person = persons.get(position);
            view.BorrowPersonName.setText(person.getPerson().getName());
            view.BorrowPersonNumber.setText(person.getPerson().getPhoneNumber());
            view.BorrowPersonDateGet.setText(
                    " " + person.getTakenDate().get(Calendar.DAY_OF_MONTH)
                            + " " + person.getTakenDate().get(Calendar.MONTH)
                            + " " + person.getTakenDate().get(Calendar.YEAR));
            view.BorrowPersonDateRepeat.setText(
                    " " + person.getReturnDate().get(Calendar.DAY_OF_MONTH)
                            + " " + person.getReturnDate().get(Calendar.MONTH)
                            + " " + person.getReturnDate().get(Calendar.YEAR));
            view.BorrowPersonSumm.setText("" + person.getAmount());
            if (person.getPerson().getPhoto().equals("")) {
                view.BorrowPersonPhotoPath.setImageResource(R.drawable.credit_icon);
            } else {
                Glide
                        .with(getContext())
                        .load(person.getPerson().getPhoto())
                        .centerCrop()
                        .crossFade()
                        .into(view.BorrowPersonPhotoPath);
            }
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),
                            ""+financeManager.getDebtBorrows().get(position).getPerson().getName()
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }

        public DebtFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_fragment, parent, false);
            return new ViewHolder(view);
        }

        public void setDataChanged(DebtBorrow person) {
            persons.add(0, person);
            notifyItemInserted(0);
        }

        public void setRemoveItem(int id) {
            persons.remove(id);
            notifyItemRemoved(id);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView BorrowPersonName;
        public TextView BorrowPersonNumber;
        public TextView BorrowPersonSumm;
        public TextView BorrowPersonDateGet;
        public TextView BorrowPersonDateRepeat;
        public CircleImageView BorrowPersonPhotoPath;

        public ViewHolder(View view) {
            super(view);
            BorrowPersonName = (TextView) view.findViewById(R.id.tvBorrowPersonName);
            BorrowPersonNumber = (TextView) view.findViewById(R.id.tvBorrowPersonNumber);
            BorrowPersonSumm = (TextView) view.findViewById(R.id.tvBorrowPersonSumm);
            BorrowPersonDateGet = (TextView) view.findViewById(R.id.tvBorrowPersonDateGet);
            BorrowPersonDateRepeat = (TextView) view.findViewById(R.id.tvBorrowPersonDateRepeat);
            BorrowPersonPhotoPath = (CircleImageView) view.findViewById(R.id.imBorrowPerson);
        }
    }
}