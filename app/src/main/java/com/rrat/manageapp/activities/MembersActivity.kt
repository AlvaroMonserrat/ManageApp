package com.rrat.manageapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMainBinding
import com.rrat.manageapp.databinding.ActivityMembersBinding
import com.rrat.manageapp.models.Board
import com.rrat.manageapp.utils.Constants

class MembersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMembersBinding
    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)!!
        }

        setupActionBar()
    }

    private fun setupActionBar(){

        val toolBar = binding.toolbarMembersActivity

        setSupportActionBar(toolBar)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionbar.title = resources.getString(R.string.members)
        }

        toolBar.setNavigationOnClickListener {  onBackPressed() }

    }
}