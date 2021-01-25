package com.marias.countdown.screens.eventlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marias.countdown.data.CountdownEvent
import com.marias.countdown.repository.CountdownRepository
import kotlinx.coroutines.launch

class EventListViewModel:ViewModel() {
    private val repo = CountdownRepository()
    val eventsLiveData = MutableLiveData<MutableList<CountdownEvent>>()
    val emptyScreenLiveData = MutableLiveData<Boolean>()

    fun getEventList(){
        viewModelScope.launch {
            val list = repo.getEvents()
            eventsLiveData.value = list
            emptyScreenLiveData.value = list.isEmpty()
        }
    }
    fun deleteEvent(event: CountdownEvent){
        viewModelScope.launch {
            repo.deleteEvent(event)
            getEventList()
        }
    }
    fun undoDelete(event: CountdownEvent){
        viewModelScope.launch {
            repo.addEvent(event)
        }
    }
}