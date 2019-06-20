package com.example.deepamgoel.newsy.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.deepamgoel.newsy.R;
import com.example.deepamgoel.newsy.util.WebUtils;

import static com.example.deepamgoel.newsy.NewsyApplication.getPreferences;

public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preference);

        Preference country = findPreference(getString(R.string.settings_country_key));
        if (country != null) {
            country.setOnPreferenceChangeListener(this);
            bindPreferenceSummaryToValue(country);
        }
        Preference defaultView = findPreference(getString(R.string.settings_default_view_key));
        if (defaultView != null) {
            defaultView.setOnPreferenceChangeListener(this);
            bindPreferenceSummaryToValue(defaultView);
        }
        Preference pageSize = findPreference(getString(R.string.setting_page_size_key));
        if (pageSize != null) {
            pageSize.setOnPreferenceChangeListener(this);
            bindPreferenceSummaryToValue(pageSize);
        }


        Preference privacyPolicy = findPreference(getString(R.string.settings_privacy_policy_key));
        if (privacyPolicy != null) {
            privacyPolicy.setOnPreferenceClickListener(this);
        }
        Preference acknowledgement = findPreference(getString(R.string.settings_acknowledgement_key));
        if (acknowledgement != null) {
            acknowledgement.setOnPreferenceClickListener(this);
        }
        Preference aboutApp = findPreference(getString(R.string.settings_about_app_key));
        if (aboutApp != null) {
            aboutApp.setOnPreferenceClickListener(this);
        }
        Preference share = findPreference(getString(R.string.settings_share_app_key));
        if (share != null) {
            share.setOnPreferenceClickListener(this);
        }
        Preference feedback = findPreference(getString(R.string.settings_feedback_key));
        if (feedback != null) {
            feedback.setOnPreferenceClickListener(this);
        }
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        String preferenceString = getPreferences().getString(preference.getKey(), "");
        onPreferenceChange(preference, preferenceString);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = newValue.toString();
        preference.setSummary(value);
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.settings_acknowledgement_key))) {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
            alert.setTitle(getString(R.string.settings_acknowledgement_label));

            WebView wv = new WebView(requireContext());
            wv.loadUrl(getString(R.string.settings_acknowledgement_url));
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            alert.setView(wv);
            alert.setNegativeButton("close", (dialog, which) -> dialog.dismiss());
            alert.show();
            return true;
        } else if (key.equals(getString(R.string.settings_privacy_policy_key))) {
            Uri uri = Uri.parse(getString(R.string.settings_privacy_policy_url));
            WebUtils.loadUrl(requireContext(), uri);
            return true;
        } else if (key.equals(getString(R.string.settings_about_app_key))) {
            // TODO: 03-05-2019
            return true;
        } else if (key.equals(getString(R.string.settings_share_app_key))) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            String apkUrl = getString(R.string.apk_url);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_share, apkUrl));
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
            return true;
        } else if (key.equals(getString(R.string.settings_feedback_key))) {
            Uri uri = Uri.parse(getString(R.string.settings_feedback_url));
            WebUtils.loadUrl(requireContext(), uri);
            return true;
        } else
            return false;
    }
}
