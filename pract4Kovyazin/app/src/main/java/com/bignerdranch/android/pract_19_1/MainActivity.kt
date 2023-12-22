package com.bignerdranch.android.pract_19_1


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var nav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav = findViewById(R.id.bottomNavigationView)
        nav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.addCrime -> {
                    loadFragment(CrimeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.listCrimes -> {

                    loadFragment(CrimeListFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        loadFragment(CrimeFragment())


    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}