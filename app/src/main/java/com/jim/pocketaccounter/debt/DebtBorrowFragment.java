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

/**
 * Created by user on 6/4/2016.
 */

public class DebtBorrowFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        restartAdapter();
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
    }

    public void restartAdapter() {
        viewPager.setAdapter(new MyAdapter(((PocketAccounter) getContext()).getSupportFragmentManager()));
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == DEBT_FRAGMENT) {
            fb.setAlpha(1 - positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return BorrowFragment.getInstance(position);
        }

        public int getCount() {
            return 3;
        }

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
