package com.marias.countdown.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marias.countdown.data.CountdownEvent
@TypeConverters(UUIDTypeConverter::class)
@Database(entities = [CountdownEvent::class], version = 1)
abstract class CountdownDatabase:RoomDatabase(){
    abstract fun dao():CountdownDAO
}