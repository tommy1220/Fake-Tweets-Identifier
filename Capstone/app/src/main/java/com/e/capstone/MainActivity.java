package com.e.capstone;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHeadlineSelectedListener, HomeFragment.OnFragmentInteractionListener, AnalyseFragment.OnHeadlineSelectedListener, AnalyseFragment.OnFragmentInteractionListener, Report.OnHeadlineSelectedListener, Report.OnFragmentInteractionListener {
    final Fragment fragment = new HomeFragment();
    final Fragment fragment1 = new AnalyseFragment();
    final Fragment fragment2 = new Report();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(fragment).commit();
                    active = fragment;
                    return true;
                case R.id.navigation_dashboard:
                    fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_notifications:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.container, fragment2, "3").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.container, fragment1, "2").hide(fragment1).commit();
        fragmentManager.beginTransaction().add(R.id.container,fragment, "1").commit();

        fragmentManager.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
