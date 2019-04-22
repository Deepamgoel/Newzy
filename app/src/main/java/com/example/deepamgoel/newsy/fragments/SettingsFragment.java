package com.example.deepamgoel.newsy.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.deepamgoel.newsy.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preference);

        Preference country = findPreference(getString(R.string.settings_country_key));
        Preference defaultView = findPreference(getString(R.string.settings_default_view_key));
        Preference pageSize = findPreference(getString(R.string.setting_page_size_key));

        bindPreferenceSummaryToValue(country);
        bindPreferenceSummaryToValue(defaultView);
        bindPreferenceSummaryToValue(pageSize);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.
                getDefaultSharedPreferences(preference.getContext());
        String preferenceString = preferences.getString(preference.getKey(), "");
        onPreferenceChange(preference, Objects.requireNonNull(preferenceString));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        preference.setSummary(value);
        return true;
    }
}
