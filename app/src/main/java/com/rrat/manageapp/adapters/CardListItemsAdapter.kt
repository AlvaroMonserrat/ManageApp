package com.rrat.manageapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rrat.manageapp.activities.TaskListActivity
import com.rrat.manageapp.databinding.ItemCardBinding

import com.rrat.manageapp.models.Card
import com.rrat.manageapp.models.SelectedMembers

open class CardListItemsAdapter(
        private val context: Context,
        private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var binding: ItemCardBinding
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            if(model.labelColor.isNotEmpty()){
                holder.binding.viewLabelColor.visibility = View.VISIBLE
                holder.binding.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.binding.viewLabelColor.visibility = View.GONE
            }

            holder.binding.tvCardName.text = model.name

            if((context as TaskListActivity).mAssignedMemberDetailsList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for(i in context.mAssignedMemberDetailsList.indices){
                    for(j in model.assignedTo){
                        if(context.mAssignedMemberDetailsList[i].id == j){
                            val selectedMembers = SelectedMembers(
                                    context.mAssignedMemberDetailsList[i].id,
                                    context.mAssignedMemberDetailsList[i].image)

                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }

                if(selectedMembersList.size > 0){
                    if(selectedMembersList.size == 1
                            && selectedMembersList[0].id == model.createdBy){
                        holder.binding.rvCardSelectedMembersList.visibility = View.GONE
                    }else{
                        holder.binding.rvCardSelectedMembersList.visibility = View.VISIBLE

                        holder.binding.rvCardSelectedMembersList.layoutManager =
                                GridLayoutManager(context, 4)

                        val adapter = CardMemberListItemsAdapter(context, selectedMembersList,
                                false)
                        holder.binding.rvCardSelectedMembersList.adapter = adapter
                        adapter.setOnClickListener(object : CardMemberListItemsAdapter
                            .OnClickListener{
                                override fun onClick() {
                                    if(onClickListener != null){
                                        onClickListener!!.onClick(position)
                                    }
                            }

                        })
                    }

                }else{
                    holder.binding.rvCardSelectedMembersList.visibility = View.GONE
                }


            }



            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root){

    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener{
        fun onClick(cardPosition: Int)
    }

}