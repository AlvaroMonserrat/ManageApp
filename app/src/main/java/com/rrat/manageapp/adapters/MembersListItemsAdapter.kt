package com.rrat.manageapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ItemMemberBinding
import com.rrat.manageapp.models.User
import com.rrat.manageapp.utils.Constants

open class MembersListItemsAdapter(
        private val context: Context,
        private var list: ArrayList<User>
        ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding: ItemMemberBinding

    private var onCLickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemMemberBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.binding.ivMemberImage)

            holder.binding.tvMemberName.text = model.name
            holder.binding.tvMemberEmail.text = model.email

            if(model.selected){
                holder.binding.ivSelectedMember.visibility = View.VISIBLE
            }else{
                holder.binding.ivSelectedMember.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onCLickListener != null){
                    if(model.selected){
                        onCLickListener!!.onClick(position, model, Constants.UN_SELECT)
                    }else{
                        onCLickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root){

    }

    fun setOnClickListener(onClickListener: MembersListItemsAdapter.OnClickListener){
        onCLickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

}