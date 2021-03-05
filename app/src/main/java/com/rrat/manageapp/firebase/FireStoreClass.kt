package com.rrat.manageapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rrat.manageapp.activities.*
import com.rrat.manageapp.models.User
import com.rrat.manageapp.utils.Constants

class FireStoreClass {

    private  val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
            mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.userRegisterSuccess()
                }.addOnFailureListener {
                    e->
                    Log.e(activity.javaClass.simpleName, "Error: {$e}")
                }
    }

    fun getCurrentUserId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if(currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .update(userHashMap)
                .addOnSuccessListener {
                    Log.e(activity.javaClass.simpleName, "Profile Data updated")
                    Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    activity.profileUpdateSuccess()
                }.addOnFailureListener {
                    e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error created a board", e)
                    Toast.makeText(activity, "Error updated profile", Toast.LENGTH_SHORT).show()
                }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS)
                .document(getCurrentUserId())
                .get()
                .addOnSuccessListener { document->
                    val loggedInUser = document.toObject(User::class.java)
                    if(loggedInUser != null){
                        when(activity){
                            is SignInActivity->{
                                activity.signInSuccess(loggedInUser)
                            }
                            is MainActivity->{
                                activity.updateNavigationUserDetails(loggedInUser)
                            }
                            is MyProfileActivity->{
                                activity.setUserDataInUT(loggedInUser)
                            }
                        }
                    }


                }.addOnFailureListener {
                    e->
                    when(activity){
                        is SignInActivity->{
                            activity.hideProgressDialog()
                        }
                        is MainActivity->{
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName, "Error: {$e}")
                }
    }

}