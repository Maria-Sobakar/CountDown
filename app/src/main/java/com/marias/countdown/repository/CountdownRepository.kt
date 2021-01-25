package com.marias.countdown.repository


import com.marias.countdown.data.CountdownEvent
import com.marias.countdown.data.database.DatabaseBuilder

class CountdownRepository {
    private val database = DatabaseBuilder.instance
    private val dao = database.dao()

    suspend fun addEvent(event: CountdownEvent) = dao.addEvent(event)

    suspend fun getEvents() = dao.getEvents()

    suspend fun deleteEvent(event: CountdownEvent) = dao.deleteEvent(event)
}