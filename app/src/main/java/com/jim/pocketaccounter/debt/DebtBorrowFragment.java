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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.debt_borrow_fragment, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tlDebtBorrowFragment);
        viewPager = (ViewPager) view.findViewById(R.id.vpDebtBorrowFragment);
        fb = (FloatingActionButton) view.findViewById(R.id.fbDebtBorrowFragment);
        fb.setOnClickListener(this);

        viewPager.setAdapter(new MyAdapter(((PocketAccounter)getContext()).getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fbDebtBorrowFragment) {
            switch (viewPager.getCurrentItem()) {
                case BORROW_FRAGMENT: {
                    ((PocketAccounter) getContext()).replaceFragment(AddBorrowFragment.getInstance(BORROW_FRAGMENT));
                    break;
                }
                case DEBT_FRAGMENT: {
                    ((PocketAccounter) getContext()).replaceFragment(AddBorrowFragment.getInstance(DEBT_FRAGMENT));
                    break;
                }
            }
        }
    }

    private class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return BorrowFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
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
