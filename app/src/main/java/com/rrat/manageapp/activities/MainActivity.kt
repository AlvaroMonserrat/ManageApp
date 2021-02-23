package com.rrat.manageapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMainBinding
import com.rrat.manageapp.firebase.FireStoreClass
import com.rrat.manageapp.models.User
import org.w3c.dom.Text
import kotlin.math.log

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        binding.navView.setNavigationItemSelectedListener(this)

        FireStoreClass().signInUser(this)

    }

    private fun setupActionBar(){

        val toolBar = binding.drawerLayout.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main_activity)
        
        setSupportActionBar(toolBar)
        toolBar.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolBar.setNavigationOnClickListener {
            //Toggle Drawer
            toggleDrawer()
        }
    }

    private fun toggleDrawer(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed(){
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_my_profile ->{
                Toast.makeText(this, "My profile", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_sign_out->{
                Toast.makeText(this, "My Sign Out", Toast.LENGTH_SHORT).show()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    fun updateNavigationUserDetails(user: User) {
        Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(binding.drawerLayout.findViewById(R.id.nave_user_image));

        binding.drawerLayout.findViewById<TextView>(R.id.tv_username).text = user.name
    }
}