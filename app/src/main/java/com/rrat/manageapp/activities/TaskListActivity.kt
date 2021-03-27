package com.rrat.manageapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.rrat.manageapp.R
import com.rrat.manageapp.adapters.TaskListItemsAdapter
import com.rrat.manageapp.databinding.ActivityTaskListBinding
import com.rrat.manageapp.firebase.FireStoreClass
import com.rrat.manageapp.models.Board
import com.rrat.manageapp.models.Card
import com.rrat.manageapp.models.Task
import com.rrat.manageapp.models.User
import com.rrat.manageapp.utils.Constants

class TaskListActivity : BaseActivity() {

    lateinit var binding: ActivityTaskListBinding
    private lateinit var mBoardDetails: Board

    private lateinit var mBoardDocumentId: String
    lateinit var mAssignedMemberDetailsList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getBoardDetails(this, mBoardDocumentId)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
                && requestCode == MEMBERS_REQUEST_CODE
                || requestCode == CARD_DETAILS_REQUEST_CODE) {

            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().getBoardDetails(this, mBoardDocumentId)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                // Se puede enviar usando putExtra porque mBoardDetails es Parcelable
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar() {

        val toolBar = binding.toolbarTaskListActivity

        setSupportActionBar(toolBar)

        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
            actionbar.title = mBoardDetails.name
        }

        toolBar.setNavigationOnClickListener { onBackPressed() }

    }

    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        val intent = Intent(this, CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION, taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION, cardPosition)

        intent.putExtra(Constants.BOARD_MEMBERS_LIST, mAssignedMemberDetailsList)

        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)
    }

    fun boardDetails(board: Board) {

        mBoardDetails = board

        hideProgressDialog()
        setupActionBar()

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)

    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()

        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String) {
        val task = Task(taskListName, FireStoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun updateTaskList(position: Int, listName: String, model: Task) {
        val task = Task(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun deleteTaskList(position: Int) {
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun addCardToTaskList(position: Int, cardName: String) {
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        val cardAssignedUserList: ArrayList<String> = ArrayList()
        cardAssignedUserList.add(FireStoreClass().getCurrentUserId())

        val card = Card(cardName, FireStoreClass().getCurrentUserId(), cardAssignedUserList)

        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)

        val task = Task(
                mBoardDetails.taskList[position].title,
                mBoardDetails.taskList[position].createdBy,
                cardsList
        )

        mBoardDetails.taskList[position] = task

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)

    }

    fun boardMembersDetailsList(list: ArrayList<User>){
        mAssignedMemberDetailsList = list

        val addTaskList = Task(resources.getString(R.string.add_list))
        mBoardDetails.taskList.add(addTaskList)

        binding.rvTaskList.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false)

        binding.rvTaskList.setHasFixedSize(true)

        val adapter = TaskListItemsAdapter(this, mBoardDetails.taskList)
        binding.rvTaskList.adapter = adapter

        hideProgressDialog()
    }

    companion object{
        const val MEMBERS_REQUEST_CODE: Int = 13
        const val CARD_DETAILS_REQUEST_CODE: Int = 14
    }

}