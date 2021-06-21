package com.example.seq3.ui

import android.os.Bundle
import android.preference.PreferenceActivity
import com.example.seq3.R

class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)


    }
}