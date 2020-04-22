package com.example.tutor;

import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SeekBarPreference;

import static java.lang.reflect.Array.getInt;


public class ProfileFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPreferences;
    ListPreference city,book,lesson;
    EditTextPreference name;
    SeekBarPreference bar;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.pref_set);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        city = (ListPreference) findPreference("city");
        book = (ListPreference) findPreference("book");
        lesson = (ListPreference) findPreference("lessons");
        name = (EditTextPreference) findPreference("name");
        bar = (SeekBarPreference) findPreference("age");
        String ncity = sharedPreferences.getString("city", " ");
        city.setSummary(ncity);
        String nbook = sharedPreferences.getString("book", " ");
        book.setSummary(nbook);
        String nlesson = sharedPreferences.getString("lessons", " ");
        lesson.setSummary(nlesson);
        String nname = sharedPreferences.getString("name", " ");
        name.setSummary(nname);
        int nbar = sharedPreferences.getInt("age", 1);
        bar.setSummary(String.valueOf(nbar));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String ncity = sharedPreferences.getString("city", " ");
        city.setSummary(ncity);
        String nbook = sharedPreferences.getString("book", " ");
        book.setSummary(nbook);
        String nlesson = sharedPreferences.getString("lessons", " ");
        lesson.setSummary(nlesson);
        String nname = sharedPreferences.getString("name", " ");
        name.setSummary(nname);
        int nbar = sharedPreferences.getInt("age", 1);
        bar.setSummary(String.valueOf(nbar));


    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
