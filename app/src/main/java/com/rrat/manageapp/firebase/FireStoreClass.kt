package com.rrat.manageapp.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rrat.manageapp.activities.*
import com.rrat.manageapp.models.Board
import com.rrat.manageapp.models.Task
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
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error: {$e}")
                }
    }


    fun getBoardDetails(activity: TaskListActivity, documentId: String){
        mFireStore.collection(Constants.BOARDS)
                .document(documentId)
                .get()
                .addOnSuccessListener {
                    document->
                    Log.i(activity.javaClass.simpleName, document.toString())

                    val board = document.toObject(Board::class.java)!!
                    board.documentId = document.id
                    //get Board details
                    activity.boardDetails(board)

                }.addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board created successfully")
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener {
                    e->
                activity.hideProgressDialog()
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

    fun addUpdateTaskList(activity: TaskListActivity, board: Board){
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
                .document(board.documentId)
                .update(taskListHashMap)
                .addOnSuccessListener {
                    Log.e(activity.javaClass.simpleName, "TaskList update successfully.")
                    activity.addUpdateTaskListSuccess()
                }.addOnFailureListener {
                    exception ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", exception)

                }
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

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
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
                                activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
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

    fun getBoardsList(activity: MainActivity){
        mFireStore.collection(Constants.BOARDS)
                .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
                .get()
                .addOnSuccessListener {
                    document->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())
                    val boardList: ArrayList<Board> = ArrayList()
                    for(i in document.documents){
                        val board = i.toObject(Board::class.java)!!
                        board.documentId = i.id
                        boardList.add(board)
                    }

                    activity.populateBoardsListToUI(boardList)
                }.addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
                }
    }

    fun getAssignedMembersListDetails(activity: MembersActivity, assignedTo: ArrayList<String>){
        mFireStore.collection(Constants.USERS)
                .whereIn(Constants.ID, assignedTo)
                .get()
                .addOnSuccessListener {
                    document->
                    Log.i(activity.javaClass.simpleName, document.documents.toString())

                    val userList: ArrayList<User> = ArrayList()

                    for(i in document.documents){
                        val user = i.toObject(User::class.java)!!
                        userList.add(user)
                    }

                    activity.setupMembersList(userList)
                }.addOnFailureListener { e->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error getting members.", e)
                }
    }

}