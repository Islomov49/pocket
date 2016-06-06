package com.jim.pocketaccounter.debt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.helper.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 6/4/2016.
 */

public class DebtBorrowFragment extends Fragment implements View.OnClickListener {
    private final int BORROW_FRAGMENT = 0;
    private final int DEBT_FRAGMENT = 1;
    private final int DEBT_BORROW_ARHIV = 2;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fb;
    private ArrayList<BorrowFragment> borrowFragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.debt_borrow_fragment, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tlDebtBorrowFragment);
        viewPager = (ViewPager) view.findViewById(R.id.vpDebtBorrowFragment);
        fb = (FloatingActionButton) view.findViewById(R.id.fbDebtBorrowFragment);
        fb.setOnClickListener(this);
        borrowFragments = new ArrayList<>();
        borrowFragments.add(new BorrowFragment());
        borrowFragments.add(new BorrowFragment());
        borrowFragments.add(new BorrowFragment());

        viewPager.setAdapter(new MyAdapter(((PocketAccounter)getContext()).getSupportFragmentManager(), borrowFragments));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbDebtBorrowFragment) {
            switch (viewPager.getCurrentItem()) {
                case BORROW_FRAGMENT: {
                    Toast.makeText(getContext(), "asda", Toast.LENGTH_SHORT).show();
                    ((PocketAccounter) getContext()).replaceFragment(new AddBorrowFragment());
                    break;
                }
                case DEBT_FRAGMENT: {

                    break;
                }
            }
        }
    }

    private class MyAdapter extends FragmentStatePagerAdapter {
        private List<BorrowFragment> borrowFragmentsl;

        public MyAdapter(FragmentManager fm, List<BorrowFragment> fragments) {
            super(fm);
            borrowFragmentsl = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return borrowFragmentsl.get(position);
        }

        @Override
        public int getCount() {
            return borrowFragmentsl.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == BORROW_FRAGMENT) {
                return "Borrows";
            }
            if (position == DEBT_FRAGMENT) {
                return "Debts";
            }
            return "Archive";
        }
    }
}
