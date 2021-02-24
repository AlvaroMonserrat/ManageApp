package com.rrat.manageapp.activities

import android.os.Bundle
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMyProfileBinding

class MyProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()
    }


    private fun setupActionBar(){

        val toolBar = binding.toolbarProfileActivity

        setSupportActionBar(toolBar)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionbar.title = resources.getString(R.string.my_profile)
        }

        toolBar.setNavigationOnClickListener {  onBackPressed() }

    }
}