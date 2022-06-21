package com.neeeel.water_drinking_assistant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neeeel.water_drinking_assistant.database.dao.ClockDao
import com.neeeel.water_drinking_assistant.database.bean.Clock

@Database(
    entities = [Clock::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDb: RoomDatabase() {

    abstract fun clockDao(): ClockDao

    //获取单例
    companion object {
        @JvmStatic
        @Volatile
        lateinit var INSTANCE: RoomDb

        private const val NAME = "my_database"

        fun init(context: Context) {
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, RoomDb::class.java, NAME)
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
            }
        }
    }
}