package com.marias.countdown.screens.eventlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marias.countdown.data.CountdownEvent
import com.marias.countdown.databinding.ActivityMainBinding
import com.marias.countdown.screens.newewent.NewEvent

class MainActivity : AppCompatActivity(), Callback {
    private val viewModel: EventListViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter = EventListAdapter(this, mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.eventListRecyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.getEventList()
        binding.eventListRecyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeAt(viewHolder.adapterPosition, binding.eventListCoordinatorLayout)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.eventListRecyclerView)
        binding.addNewCountdownFAB.setOnClickListener {
            val intent = Intent(this, NewEvent::class.java)
            startActivity(intent)
        }
        viewModel.eventsLiveData.observe({ this.lifecycle }) {
            if (it.isEmpty()){
                binding.emptyEventListFrameLayout.isVisible = true
            } else{
                binding.emptyEventListFrameLayout.isVisible = false
                adapter.eventList = it
                binding.eventListRecyclerView.adapter = adapter
            }
        }
        viewModel.emptyScreenLiveData.observe({this.lifecycle}){
            binding.emptyEventListFrameLayout.isVisible = it
        }
    }

    override fun deleteEvent(event: CountdownEvent) {
        viewModel.deleteEvent(event)
    }

    override fun undoDelete(event: CountdownEvent) {
        viewModel.undoDelete(event)
    }

}
