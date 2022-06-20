package com.neeeel.water_drinking_assistant

import android.app.Application
import com.neeeel.water_drinking_assistant.database.RoomDb

class App: Application() {

    companion object  {
        lateinit var INSTANCE: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        // Room 数据库初始化
        RoomDb.init(this)
    }
}