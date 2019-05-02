package com.example.deepamgoel.newsy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.deepamgoel.newsy.NewsyApplication;
import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.ui.bookmark.BookmarksFragment;
import com.example.deepamgoel.newsy.ui.home.HomeFragment;
import com.example.deepamgoel.newsy.ui.search.SearchFragment;
import com.example.deepamgoel.newsy.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final Fragment mHomeFragment = new HomeFragment();
    private final Fragment mSearchFragment = new SearchFragment();
    private final Fragment mBookmarkFragment = new BookmarksFragment();
    private final Fragment mSettingsFragment = new SettingsFragment();

    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.bottom_navigation_main)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadFragment(mHomeFragment);
        mBottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    fragment = mHomeFragment;
                    break;
                case R.id.action_search:
                    fragment = mSearchFragment;
                    break;
                case R.id.action_bookmark:
                    fragment = mBookmarkFragment;
                    break;
                case R.id.action_settings:
                    fragment = mSettingsFragment;
                    break;
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_main, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NewsyApplication.getAppContext().mustDie(this);
    }

}
