package com.bignerdranch.android.pract_19_1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.pract_19_1.CrimeDatabase
import com.bignerdranch.android.pract_19_1.migration_1_2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {
    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2).build()
    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()
    fun getCrimes(): LiveData<List<Crime>>? = crimeDao.getCrimes()
    fun getCrime(id: UUID): LiveData<Crime?>? = crimeDao.getCrime(id)
    fun updateCrime(crime: Crime) {
        executor.execute {
            crimeDao.getCrime(crime.id)
        }
    }

    suspend fun getCrimeByTitle(title: String): Crime? {
        return withContext(Dispatchers.IO) {
            crimeDao.getCrimeByTitle(title)
        }
    }

    fun addCrime(crime: Crime) {
        executor.execute {
            crimeDao.addCrime(crime)
        }
    }

    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
