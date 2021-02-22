package com.rrat.manageapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


    }

    private fun setupActionBar(){
        setSupportActionBar(binding.drawerLayout.findViewById(R.id.toolbar_main_activity))
    }
}