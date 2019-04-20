package com.example.deepamgoel.newsy.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.fragments.BookmarkFragment;
import com.example.deepamgoel.newsy.fragments.HomeFragment;
import com.example.deepamgoel.newsy.fragments.SearchFragment;
import com.example.deepamgoel.newsy.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation_main)
    BottomNavigationView mBottomNavigationView;

    private Fragment mHomeFragment = new HomeFragment();
    private Fragment mSearchFragment = new SearchFragment();
    private Fragment mBookmarkFragment = new BookmarkFragment();
    private Fragment mSettingsFragment = new SettingsFragment();

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
        // TODO: 12-04-2019 Pagination, Searching
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
}
