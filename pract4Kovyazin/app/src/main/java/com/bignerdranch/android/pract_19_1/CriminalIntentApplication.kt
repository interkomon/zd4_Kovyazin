package com.bignerdranch.android.pract_19_1

import android.app.Application

class CriminalIntentApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}
