package com.jim.pocketaccounter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.report.FilterDialog;
import com.jim.pocketaccounter.report.FilterSelectable;

import java.util.ArrayList;
import java.util.Calendar;


public class ReportByCategory extends Fragment {
    private ImageView ivToolbarMostRight;
    private FilterDialog filterDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_category, container, false);
        ViewPager vpReportByCategory = (ViewPager) rootView.findViewById(R.id.vpReportByCategoryPager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tlReportByCategoryTab);
        final ReportByCategoryIncomesFragment incomesFragment = new ReportByCategoryIncomesFragment();
        final ReportByCategoryExpansesFragment expansesFragment = new ReportByCategoryExpansesFragment();
        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.ic_filter);
        ivToolbarMostRight.setVisibility(View.VISIBLE);
        filterDialog = new FilterDialog(getContext());
        filterDialog.setOnDateSelectedListener(new FilterSelectable() {
            @Override
            public void onDateSelected(Calendar begin, Calendar end) {
                incomesFragment.invalidate(begin, end);
            }
        });
        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.show();
            }
        });
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(incomesFragment);
        list.add(expansesFragment);
        PocketAccounter.toolbar.setTitle(R.string.report_by_categories);
        PocketAccounter.toolbar.setSubtitle("");
        vpReportByCategory.setAdapter(new MyViewPagerAdapter(((PocketAccounter) getContext()).getSupportFragmentManager(), list));
        tabLayout.setupWithViewPager(vpReportByCategory);
        vpReportByCategory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    filterDialog.setOnDateSelectedListener(new FilterSelectable() {
                        @Override
                        public void onDateSelected(Calendar begin, Calendar end) {
                            incomesFragment.invalidate(begin, end);
                        }
                    });
                }
                else {
                    filterDialog.setOnDateSelectedListener(new FilterSelectable() {
                        @Override
                        public void onDateSelected(Calendar begin, Calendar end) {
                            expansesFragment.invalidate(begin, end);
                        }
                    });
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                return getResources().getString(R.string.income);
            return getResources().getString(R.string.expanse);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight).setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight).setVisibility(View.VISIBLE);
    }
}