package com.rrat.manageapp.activities

import android.os.Bundle
import com.bumptech.glide.Glide
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMyProfileBinding
import com.rrat.manageapp.firebase.FireStoreClass
import com.rrat.manageapp.models.User

class MyProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        FireStoreClass().loadUserData(this)
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

    fun setUserDataInUT(user: User){
        Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(binding.ivUserImage)

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if(user.mobile != 0L){
            binding.etPhone.setText(user.mobile.toString())
        }
    }
}