package com.marias.countdown.screens.eventlist

import com.marias.countdown.data.CountdownEvent

interface Callback {
    fun deleteEvent(event: CountdownEvent)
    fun undoDelete(event: CountdownEvent)
}