package com.neeeel.water_drinking_assistant.database.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clock(
    @PrimaryKey(autoGenerate = true)
    var id: Long = -1,
    var title: String = "",
    var interval: Int = Int.MAX_VALUE,
)