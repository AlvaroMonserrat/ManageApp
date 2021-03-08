package com.rrat.manageapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rrat.manageapp.R
import com.rrat.manageapp.databinding.ItemBoardBinding
import com.rrat.manageapp.models.Board

open class BoardItemsAdapter(private val context: Context,
                             private var list: ArrayList<Board>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var binding: ItemBoardBinding

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if(holder is MyViewHolder){
            Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_board_place_holder)
                    .into(holder.itemImageViewCircle)

            holder.itemTextViewName.text = model.name
            holder.itemTextViewCreatedBy.text = "Created by: ${model.createdBy}"

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root){
        val itemImageViewCircle = binding.ivBoardImage
        val itemTextViewName = binding.tvName
        val itemTextViewCreatedBy = binding.tvCreatedBy
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface  OnClickListener{
        fun onClick(position: Int, model: Board)
    }
}