package com.neeeel.water_drinking_assistant.database.dao

import androidx.room.*
import com.neeeel.water_drinking_assistant.database.bean.Clock

@Dao
interface ClockDao {

    @Query("SELECT * FROM clock WHERE id = :id")
    fun queryById(id: Long): Clock

    @Query("SELECT * FROM clock")
    fun all(): List<Clock>

    @Query("SELECT COUNT(*) FROM clock")
    fun count(): Int

    @Insert
    fun insert(clock: Clock)

    @Update
    fun update(clock: Clock)

    @Delete
    fun delete(clock: Clock)
}