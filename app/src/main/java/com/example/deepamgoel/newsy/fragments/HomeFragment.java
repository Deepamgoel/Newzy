package com.example.deepamgoel.newsy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class HomeFragment extends Fragment {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private ViewPagerAdapter adapter;
    private List<String> sectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sectionList = Arrays.asList(getResources().getStringArray(R.array.category_array));
        sectionList.set(0, getPreferences().getString(getString(R.string.settings_country_key), getString(R.string.settings_country_india_value)));

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.getTabAt(0).setText(getPreferences().getString(getString(R.string.settings_country_key), getString(R.string.settings_country_india_value)));
    }

    private void setUpPage() {
        for (int i = 0; i < sectionList.size(); i++) {
            Fragment fragment = NewsListFragment.newInstance(i, sectionList.get(i));
            adapter.addFragment(fragment, sectionList.get(i));
            adapter.notifyDataSetChanged();
        }
    }
}
