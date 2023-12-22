package com.bignerdranch.android.pract_19_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class CrimeDatailModel(): ViewModel() {
    private val repository=CrimeRepository.get()

    suspend fun getCrimeByTitle(title: String): Crime? {
        return repository.getCrimeByTitle(title)
    }

    fun save(crime: Crime) {
        repository.updateCrime(crime)
    }
    fun add(crime: Crime){
        repository.addCrime(crime)
    }
}