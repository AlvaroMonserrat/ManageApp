package com.rrat.manageapp.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}