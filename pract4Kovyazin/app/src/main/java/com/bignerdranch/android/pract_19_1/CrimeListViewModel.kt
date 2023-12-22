package com.bignerdranch.android.pract_19_1

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel() {

    val crimeRepository=CrimeRepository.get()
    val crimeListLiveData=crimeRepository.getCrimes()
}