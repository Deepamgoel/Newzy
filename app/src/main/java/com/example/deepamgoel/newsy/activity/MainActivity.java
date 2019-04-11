package com.example.deepamgoel.newsy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.deepamgoel.newsy.BuildConfig;
import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.fragment.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private static final String guardianApiKey = BuildConfig.GuardianApiKey;
    public static final String REQUESTED_URL =
            "https://content.guardianapis.com/search?q=&format=json&show-fields=headline,thumbnail,short-url&show-tags=contributor,publication&page-size=5&api-key=" + guardianApiKey;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout_main, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}