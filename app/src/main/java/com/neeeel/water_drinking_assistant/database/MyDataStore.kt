package com.neeeel.water_drinking_assistant.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.neeeel.water_drinking_assistant.database.MyDataStore.DATA_STORE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object MyDataStore {
    const val DATA_STORE_NAME = "init_info"
    const val INIT = "init"
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

fun DataStore<Preferences>.getInt(name: String): Flow<Int> {
    return this.data.map {
        it[intPreferencesKey(name)]?: 0
    }
}



