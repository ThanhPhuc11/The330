package com.nagaja.the330.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.nagaja.the330.model.AuthTokenModel
import com.nagaja.the330.model.UserDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "THE330")

class DataStorePref(val context: Context) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
        val USER_DETAIL = stringPreferencesKey("USER_DETAIL")
    }

    fun setUserDetail(obj: UserDetail?) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { set ->
                if (obj != null) {
                    val json = Gson().toJson(obj)
                    set[USER_DETAIL] = json
                } else {
                    set[USER_DETAIL] = ""
                }
            }
        }
    }

    fun setToken(obj: AuthTokenModel?) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { set ->
                if (obj != null) {
                    val json = Gson().toJson(obj)
                    set[AUTH_TOKEN] = json
                } else {
                    set[AUTH_TOKEN] = ""
                }
            }
        }
    }
}