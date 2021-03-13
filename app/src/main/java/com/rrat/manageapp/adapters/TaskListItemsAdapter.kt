package com.rrat.manageapp.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.rrat.manageapp.databinding.ItemBoardBinding
import com.rrat.manageapp.databinding.ItemTaskBinding
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
        }
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    private class MyViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root){
        val itemViewTvAddTaskList = binding.tvAddTaskList
        val itemViewLinearLayoutTaskItem = binding.llTaskItem
    }

    private fun Int.toDP(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}