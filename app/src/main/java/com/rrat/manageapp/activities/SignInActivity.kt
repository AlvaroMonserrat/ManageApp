package com.rrat.manageapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setWindowFullScreen()
        setupActionBar()

        binding.btnSignIn.setOnClickListener {
            signInRegisterUser()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarSignInActivity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarSignInActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun signInRegisterUser(){
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        task ->
                        hideProgressDialog()
                        if(task.isSuccessful){
                            val firebaseUser : FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            Toast.makeText(this, "You " +
                                    "succesfully Sign In " +
                                    "the email address $registeredEmail",
                                    Toast.LENGTH_LONG)
                                    .show()
                            startActivity(Intent(this, MainActivity::class.java))
                        }else{
                            Toast.makeText(this, "Authentication failed.",
                                    Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }

        }
    }

    private fun validateForm(email: String, password: String): Boolean{
        return when {
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



}