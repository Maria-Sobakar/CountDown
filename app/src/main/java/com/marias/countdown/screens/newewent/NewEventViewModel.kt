package com.marias.countdown.screens.newewent

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marias.countdown.Event
import com.marias.countdown.data.CountdownEvent
import com.marias.countdown.repository.CountdownRepository
import com.marias.countdown.screens.eventlist.MainActivity
import kotlinx.coroutines.launch
import java.util.*

class NewEventViewModel : ViewModel() {
    private var minute =0
    private var hour = 0
    private var day = 0
    private var month = 0
    private var year =0
    private var repo = CountdownRepository()
    private var event: CountdownEvent = CountdownEvent()
    private var targetTime = Calendar.getInstance()
    val invalidTimeLiveData = MutableLiveData<Event<Boolean>>()

    fun timeIsSet(time: Calendar) {
         hour = time.get(Calendar.HOUR_OF_DAY)
         minute = time.get(Calendar.MINUTE)
    }

    fun addEvent(context:Context) {
        viewModelScope.launch {
            targetTime.set(year,month,day,hour,minute)
            val currentTime = System.currentTimeMillis()
            if ((targetTime.timeInMillis+1000*60)>currentTime){
                event.time = targetTime.timeInMillis
                repo.addEvent(event)
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } else{
                invalidTimeLiveData.value = Event(true)
            }
        }
    }

    fun dateIsSet(date: Calendar) {
         day = date.get(Calendar.DAY_OF_MONTH)
         month = date.get(Calendar.MONTH)
         year = date.get(Calendar.YEAR)
    }

    fun nameChanged(text: CharSequence?) {
        event.title = text.toString()
    }
}