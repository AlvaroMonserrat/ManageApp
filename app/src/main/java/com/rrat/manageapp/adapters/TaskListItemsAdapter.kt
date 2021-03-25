package com.rrat.manageapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rrat.manageapp.activities.TaskListActivity
import com.rrat.manageapp.databinding.ItemBoardBinding
import com.rrat.manageapp.databinding.ItemTaskBinding
import com.rrat.manageapp.models.Card
import com.rrat.manageapp.models.Task

open class TaskListItemsAdapter(private val context: Context,
                                private val listTask: ArrayList<Task>
                                ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: ItemTaskBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = LinearLayout.LayoutParams(
                (parent.width * 0.7).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(15.toDP().toPx(), 0, (40).toDP().toPx(), 0)

        binding.root.layoutParams = layoutParams

        return TaskListItemsAdapter.MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = listTask[position]
        if(holder is MyViewHolder){
            if(position == listTask.size - 1 ){
                holder.itemViewTvAddTaskList.visibility = View.VISIBLE
                holder.itemViewLinearLayoutTaskItem.visibility = View.GONE
            }else{
                holder.itemViewTvAddTaskList.visibility = View.GONE
                holder.itemViewLinearLayoutTaskItem.visibility = View.VISIBLE
            }

            holder.itemViewTaskListTitle.text = model.title
            holder.itemViewTvAddTaskList.setOnClickListener {
                holder.itemViewTvAddTaskList.visibility = View.GONE
                holder.itemViewCvAddTaskListName.visibility = View.VISIBLE
            }

            holder.imageButtonCloseListName.setOnClickListener {
                holder.itemViewTvAddTaskList.visibility = View.VISIBLE
                holder.itemViewCvAddTaskListName.visibility = View.GONE
            }


            holder.imageButtonDoneListName.setOnClickListener {
                Log.i("EditText", holder.itemViewEditTaskListName.id.toString())
                val listName = holder.itemViewEditTaskListName.text.toString()

                if(listName.isNotEmpty()){
                    if(context is TaskListActivity){
                        context.createTaskList(listName)
                    }
                }else{
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                }


            }

            holder.binding.ibEditListName.setOnClickListener {
                holder.binding.etEditTaskListName.setText(model.title)
                holder.binding.llTitleView.visibility = View.GONE
                holder.binding.cvEditTaskListName.visibility = View.VISIBLE
            }

            holder.binding.ibCloseEditableView.setOnClickListener {
                holder.binding.llTitleView.visibility = View.VISIBLE
                holder.binding.cvEditTaskListName.visibility = View.GONE
            }

            holder.binding.ibDoneEditListName.setOnClickListener {

                val listName = holder.binding.etEditTaskListName.text.toString()

                if(listName.isNotEmpty()){
                    if(context is TaskListActivity){
                        context.updateTaskList(position, listName, model)
                    }
                }else{
                    Toast.makeText(context, "Please Enter List Name.", Toast.LENGTH_SHORT).show()
                }

            }

            holder.binding.ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(position, model.title)
            }

            holder.binding.tvAddCard.setOnClickListener{
                holder.binding.tvAddCard.visibility = View.GONE
                holder.binding.cvAddCard.visibility = View.VISIBLE
            }

            holder.binding.ibCloseCardName.setOnClickListener {
                holder.binding.tvAddCard.visibility = View.VISIBLE
                holder.binding.cvAddCard.visibility = View.GONE
            }

            holder.binding.ibDoneCardName.setOnClickListener {

                val cardName = holder.binding.etCardName.text.toString()

                if(cardName.isNotEmpty()){
                    if(context is TaskListActivity){
                        // Todo add a card
                        context.addCardToTaskList(position, cardName)
                    }
                }else{
                    Toast.makeText(context, "Please Enter Card Name.", Toast.LENGTH_SHORT).show()
                }

            }

            holder.binding.rvCardList.layoutManager = LinearLayoutManager(context)
            holder.binding.rvCardList.setHasFixedSize(true)

            val adapter = CardListItemsAdapter(context, model.cards)
            holder.binding.rvCardList.adapter = adapter

            adapter.setOnClickListener(object : CardListItemsAdapter.OnClickListener{
                override fun onClick(cardPosition: Int) {
                    if (context is TaskListActivity){
                        context.cardDetails(position, cardPosition)
                    }
                }

            })

        }

    }

    private fun alertDialogForDeleteList(position: Int, title: String){

        val builder = AlertDialog.Builder(context)
        // set title for alert dialog
        builder.setTitle("Alert")
        // set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        // performing positive action
        builder.setPositiveButton("Yes"){
            dialogInterface, which ->
            dialogInterface.dismiss()
                if(context is TaskListActivity){
                    context.deleteTaskList(position)
                }
        }

        builder.setNegativeButton("No"){
            dialogInterface, which ->
            dialogInterface.dismiss()
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    private class MyViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        val itemViewTvAddTaskList = binding.tvAddTaskList
        val itemViewLinearLayoutTaskItem = binding.llTaskItem
        val itemViewTaskListTitle = binding.tvTaskListTitle
        val itemViewCvAddTaskListName = binding.cvAddTaskListName
        val imageButtonCloseListName = binding.ibCloseListName
        val imageButtonDoneListName = binding.ibDoneListName
        val itemViewEditTaskListName = binding.etTaskListName
    }

    private fun Int.toDP(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}