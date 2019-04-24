package com.example.deepamgoel.newsy.fragments;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.deepamgoel.newsy.R;

import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

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
        String preferenceString = getPreferences().getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceString);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        preference.setSummary(value);
        return true;
    }
}
