package com.neeeel.water_drinking_assistant.database.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clock(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var title: String = "Title",
    var interval: Int = 100,
    var maxLines: Int = 2
)