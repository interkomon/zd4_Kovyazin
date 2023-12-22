package com.bignerdranch.android.pract_19_1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.pract_19_1.Crime
import java.util.*

@Dao
interface CrimeDao {
    @Query("SELECT * FROM crime")
    fun getCrimes(): LiveData<List<Crime>>
    @Query("SELECT * FROM crime WHERE id=(:id)")
    fun getCrime(id: UUID): LiveData<Crime?>
    @Update
    fun updateCrime(crime: Crime)
    @Insert
    fun addCrime(crime: Crime)
    @Query("SELECT * FROM crime WHERE title = :title")
    suspend fun getCrimeByTitle(title: String): Crime?
}