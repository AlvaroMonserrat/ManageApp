package com.rrat.manageapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setWindowFullScreen()
        setupActionBar()

    }

    private fun registerUser(){
        val name: String = binding.etName.text.toString().trim{ it <= ' ' }
        val email: String = binding.etEmail.text.toString().trim{ it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim{ it <= ' ' }

        if(validateForm(name, email, password)){
            Toast.makeText(this, "Now we can register a new user.", Toast.LENGTH_SHORT).show()
        }


    }

    private fun validateForm(name: String, email: String, password: String): Boolean{
        return when {
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter a email")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password")
                false
            }else ->{
                true
            }
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarSignUpActivity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarSignUpActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnSignUp.setOnClickListener{
            registerUser()
        }
    }

}