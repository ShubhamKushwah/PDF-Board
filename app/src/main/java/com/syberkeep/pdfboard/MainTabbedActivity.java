package com.syberkeep.pdfboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainTabbedActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Get intent of this activity.
        Intent intent = this.getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        //Set the shared preferences to default values.
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String path = intent.getStringExtra("path");
        String name = intent.getStringExtra("name");

        if (path == null || name == null) {
            sharedPref.edit().putString("menu", "no").apply();
        } else {
            setTitle(name);
            sharedPref.edit().putString("pathPDF", path).apply();
            sharedPref.edit().putString("title", name).apply();
        }

        if (sharedPref.getBoolean ("folderDef", false)){
            sharedPref.edit().putString("folder", "/Android/data/com.syberkeep.pdfboard/").apply();
        }

        String folder = sharedPref.getString("folder", "/Android/data/com.syberkeep.pdfboard/");

        File directory = new File(Environment.getExternalStorageDirectory() + folder + "/pdf_backups/");
        if (!directory.exists()) {
            boolean is = directory.mkdirs();

            if(is)
                Toast.makeText(MainTabbedActivity.this, "Folder created!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainTabbedActivity.this, "Unable to create folder!", Toast.LENGTH_SHORT).show();
        }

        File imgFolder = new File(Environment.getExternalStorageDirectory() + "/Pictures/.pdf_temp/");
        if (!imgFolder.exists()) {
            boolean is = imgFolder.mkdirs();
            if(is)
                Toast.makeText(MainTabbedActivity.this, "Pics Folder created!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainTabbedActivity.this, "Unable to create pics folder!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
                case 2:
                    Tab3 tab3 = new Tab3();
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Just Working";
                case 1:
                    return "Text to PDF";
                case 2:
                    return "Image";
            }
            return null;
        }
    }
}
