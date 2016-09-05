package com.androidbegin.parselogintutorial;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

/**
 * Created by Emi on 12.06.2015.
 */
public class BeginHere  extends FragmentActivity implements
        ActionBar.TabListener {
    private ViewPager viewPager;
    static TabsPagerAdapter mAdapter;
    static ActionBar actionBar;
    ParseApplication globalVariable;

    final int[] ICONS = new int[] {
            R.drawable.status,
            R.drawable.together,
            R.drawable.graphic,
            R.drawable.menu_settings
    };

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        // Get the view from main.xml
        globalVariable = (ParseApplication) getApplicationContext();
        getActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_begin_here);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);

        actionBar = getActionBar();
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b000000")));
        // Adding Tabs
        for (int i=0; i < ICONS.length; i++) {
            actionBar.addTab(actionBar.newTab()
                    .setIcon(BeginHere.this.getResources().getDrawable(ICONS[i]))
                    .setTabListener(this));

        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //cand se schimba pagina, fa ca tabul ala sa fie selectat
                actionBar.setSelectedNavigationItem(position);
                Log.d("TAAAAAB", "" + position);
                /*Spinner spinner = getTabSpinner();
                if (spinner != null) {
                    spinner.setSelection(position);
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        int x=tab.getPosition();
        Log.d("TAAAAAB",""+x);
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public ParseApplication getGlobalVar(){
        return globalVariable;
    }
}
