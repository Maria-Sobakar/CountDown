package com.marias.countdown.data.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private const val DATABASE_NAME = "countdown-database"
    lateinit var instance: CountdownDatabase

    fun initialize(context: Context) {
        synchronized(CountdownDatabase::class.java) {
            instance = buildRoomDB(context)
        }
    }

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            CountdownDatabase::class.java,
            DATABASE_NAME
        ).build()
}