package com.neeeel.water_drinking_assistant

import android.app.Application
import com.neeeel.water_drinking_assistant.database.RoomDb

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // Room 数据库初始化
        RoomDb.init(this)
    }
}