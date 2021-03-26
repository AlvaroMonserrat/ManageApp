package com.rrat.manageapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rrat.manageapp.databinding.ItemCardBinding
import com.rrat.manageapp.databinding.ItemLabelColorBinding

open class LabelColorListItemsAdapter(
        private val context: Context,
        private var list: ArrayList<String>,
        private val mSelectedColor:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    lateinit var binding: ItemLabelColorBinding

    var onItemCLickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemLabelColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if(holder is MyViewHolder){
            holder.binding.viewMain.setBackgroundColor(Color.parseColor(item))
            if(item == mSelectedColor){
                holder.binding.ivSelectedColor.visibility = View.VISIBLE
            }else{
                holder.binding.ivSelectedColor.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onItemCLickListener != null){
                    onItemCLickListener!!.onClick(position, item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(val binding: ItemLabelColorBinding): RecyclerView.ViewHolder(binding.root){

    }

    fun setOnClickListener(onClickListener: LabelColorListItemsAdapter.OnItemClickListener){
        this.onItemCLickListener = onClickListener
    }

    interface OnItemClickListener{
        fun onClick(position: Int, color: String)
    }

}