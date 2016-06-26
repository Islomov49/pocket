package com.jim.pocketaccounter;

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

import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;


public class ReportByCategory extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_category, container, false);
        ViewPager vpReportByCategory = (ViewPager) rootView.findViewById(R.id.vpReportByCategoryPager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tlReportByCategoryTab);

        ReportByCategoryIncomesFragment incomesFragment = new ReportByCategoryIncomesFragment();
        ReportByCategoryExpansesFragment expansesFragment = new ReportByCategoryExpansesFragment();

        ArrayList<Fragment> list = new ArrayList<>();
        list.add(incomesFragment);
        list.add(expansesFragment);
        PocketAccounter.toolbar.setTitle("");
        PocketAccounter.toolbar.setSubtitle("");

        vpReportByCategory.setAdapter(new MyViewPagerAdapter(
                ((PocketAccounter) getContext())
                        .getSupportFragmentManager(), list));

        tabLayout.setupWithViewPager(vpReportByCategory);
        return rootView;
    }

    private class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Fragment> fragments;
        public MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
        public int getCount() {
            return 2;
        }
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "incomes";
            return "expanses";
        }
    }
}
