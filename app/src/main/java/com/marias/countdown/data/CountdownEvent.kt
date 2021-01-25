package com.marias.countdown.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "event")
data class CountdownEvent(
    var title:String = "",
    var time:Long = 0,
    @PrimaryKey var id:UUID = UUID.randomUUID(),
)