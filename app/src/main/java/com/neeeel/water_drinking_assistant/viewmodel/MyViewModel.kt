package com.neeeel.water_drinking_assistant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import kotlin.concurrent.thread

class MyViewModel: ViewModel() {

    private val allClocks: MutableLiveData<List<Clock>> by lazy {
        MutableLiveData<List<Clock>>().also {
            load()
        }
    }

    fun getClocks(): LiveData<List<Clock>> {
        return allClocks
    }

    fun load() = thread {
        allClocks.postValue(RoomDb.INSTANCE.clockDao().all())
    }
}