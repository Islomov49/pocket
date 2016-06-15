package com.jim.pocketaccounter.report;

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

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
/**
 * Created by user on 6/15/2016.
 */

public class TableBarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_bar_fragment_layout, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vpTableBarFragment);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tlTableBarFragment);


        viewPager.setAdapter(new TableBarAdapter(((PocketAccounter)getContext()).getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private class TableBarAdapter extends FragmentStatePagerAdapter {
        public TableBarAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int position) {
            if (position == 0) {
                return new Fragment();
            }
            return new Fragment();
        }
        public int getCount() {return 2;}

        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Table";
            }
            return "Bar";
        }
    }
}
