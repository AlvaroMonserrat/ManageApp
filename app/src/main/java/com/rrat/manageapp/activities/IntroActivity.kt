package com.rrat.manageapp.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.rrat.manageapp.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setWindowFullScreen()

    }

    fun onSignIn(view: View) {
        intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    fun onSignUp(view: View) {
        intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}