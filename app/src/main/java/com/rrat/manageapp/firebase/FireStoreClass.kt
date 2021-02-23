package com.rrat.manageapp.firebase

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rrat.manageapp.activities.BaseActivity
import com.rrat.manageapp.activities.MainActivity
import com.rrat.manageapp.activities.SignInActivity
import com.rrat.manageapp.activities.SignUpActivity
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

    fun signInUser(activity: Activity){
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