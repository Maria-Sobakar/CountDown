package com.marias.countdown.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.marias.countdown.data.CountdownEvent
import java.util.*

@Dao
interface CountdownDAO {

    @Insert
    suspend fun addEvent(event: CountdownEvent)

    @Query("select * from event")
    suspend fun getEvents():MutableList<CountdownEvent>

    @Delete
    suspend fun deleteEvent(event:CountdownEvent)
}