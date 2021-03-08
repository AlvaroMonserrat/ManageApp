package com.rrat.manageapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rrat.manageapp.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    lateinit var binding: ActivityTaskListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskListBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}