package com.android.entrepreneurclub.Activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.entrepreneurclub.Adapter.TabAdapter;
import com.android.entrepreneurclub.R;

public class RegisterActivity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager pager;
    private TabAdapter mAdapter;
    private Toolbar mToolbar;

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pager=findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mAdapter = new TabAdapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);
        ;
        pager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

}
