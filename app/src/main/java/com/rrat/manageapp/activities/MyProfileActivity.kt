package com.rrat.manageapp.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ActivityMyProfileBinding
import com.rrat.manageapp.firebase.FireStoreClass
import com.rrat.manageapp.models.User
import com.rrat.manageapp.utils.Constants
import java.io.IOException
import java.util.jar.Manifest

class MyProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails: User
    private var mProfileImageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        FireStoreClass().loadUserData(this)

        binding.btnUpdate.setOnClickListener {
            if (mSelectedImageFileUri != null){
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                updateUserProfileData()
            }
        }
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

        mUserDetails = user

        Glide
                .with(this)
                .load(user.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(binding.ivProfileImage)

        binding.etName.setText(user.name)
        binding.etEmail.setText(user.email)
        if(user.mobile != 0L){
            binding.etPhone.setText(user.mobile.toString())
        }
    }

    fun onChangeImage(view: View) {
        // Preguntar por permisos y abrir gallería
        if(ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED){

            Constants.showImageChooser(this)

        }else{
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Constants.showImageChooser(this)
            }else{
                Snackbar.make(binding.root, "Oops, you just denied the permission for storage", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK
                && requestCode == PICK_IMAGE_REQUEST_CODE
                && data!!.data != null){
            mSelectedImageFileUri = data.data

            try{
                Glide
                        .with(this)
                        .load(mSelectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(binding.ivProfileImage)
            }catch(e: IOException){
                e.printStackTrace()
            }

        }
    }

    private fun updateUserProfileData(){
        val userHashMap = HashMap<String, Any>()
        var anyChangedMade = false

        if(mProfileImageURL.isNotEmpty() && mProfileImageURL  != mUserDetails.image){
            userHashMap[Constants.IMAGE] = mProfileImageURL
            anyChangedMade = true
        }

        if(binding.etName.text.toString() != mUserDetails.name){
            userHashMap[Constants.NAME] = binding.etName.text.toString()
            anyChangedMade = true
        }

        if(binding.etPhone.text.toString() != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = binding.etPhone.text.toString().toLong()
            anyChangedMade = true
        }

        if(anyChangedMade){
            FireStoreClass().updateUserProfileData(this, userHashMap)
        }

    }

    private fun uploadUserImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if(mSelectedImageFileUri != null){
            val sRef : StorageReference = FirebaseStorage.getInstance()
                    .reference
                    .child("USER_IMAGE"
                            + System.currentTimeMillis()
                            + "."
                            + Constants.getFileExtension(this, mSelectedImageFileUri!!))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot ->
                Log.i("Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri->
                    Log.i("Downloadable Image URL",
                            uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                    hideProgressDialog()


                }.addOnFailureListener {
                    exception ->
                    Snackbar.make(binding.root, exception.message.toString(), Snackbar.LENGTH_SHORT).show()
                    hideProgressDialog()
                }
            }
        }

    }



    fun profileUpdateSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

}