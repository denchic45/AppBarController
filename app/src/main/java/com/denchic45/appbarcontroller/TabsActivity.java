package com.denchic45.appbarcontroller;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.denchic45.library.AppBarController;
import com.google.android.material.tabs.TabLayout;

public class TabsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        AppBarController.create(this, findViewById(R.id.appbar));
        ViewPager vp = findViewById(R.id.viewpager);
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        TabLayout tl = findViewById(R.id.tabs);
        tl.setupWithViewPager(vp, true);
    }

    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = CellFragment.newInstance("1");
                    break;
                case 1:
                    fragment = CellFragment.newInstance("2");
                    break;
                case 2:
                    fragment = CellFragment.newInstance("3");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Fragment 1";
                case 1:
                    return "Fragment 2";
                case 2:
                    return "Fragment 3";
            }
            return null;
        }

    }
}