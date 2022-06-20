package com.neeeel.water_drinking_assistant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import kotlin.concurrent.thread

class MyViewModel: ViewModel() {

    private val clocks: MutableLiveData<List<Clock>> by lazy {
        MutableLiveData<List<Clock>>().also {
            load()
        }
    }

    fun getClocks(): LiveData<List<Clock>> {
        return clocks
    }

    fun load() = thread {
        clocks.postValue(RoomDb.INSTANCE.clockDao().all())
    }
}