package com.rrat.manageapp.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rrat.manageapp.adapters.MembersListItemsAdapter
import com.rrat.manageapp.databinding.DialogListBinding
import com.rrat.manageapp.models.User

abstract class MembersListDialog(
        context: Context,
        val list: ArrayList<User>,
        private val title: String = ""
) : Dialog(context){

    private var adapter: MembersListItemsAdapter? = null
    private lateinit var binding: DialogListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView()

    }


    private fun setUpRecyclerView(){
        binding.tvTitle.text = title

        if(list.size > 0){
            binding.rvList.layoutManager = LinearLayoutManager(context)
            adapter = MembersListItemsAdapter(context, list)
            binding.rvList.adapter = adapter

            adapter!!.setOnClickListener(object : MembersListItemsAdapter.OnClickListener{
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }

            })
        }


    }

    protected abstract fun onItemSelected(user:User, action: String)

}