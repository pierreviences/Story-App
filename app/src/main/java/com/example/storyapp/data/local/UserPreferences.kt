package com.example.storyapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_user")
class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val userIdKey = stringPreferencesKey(USER_ID_KEY)
    private val userNameKey = stringPreferencesKey(USER_NAME_KEY)
    private val userTokenKey = stringPreferencesKey(USER_TOKEN_KEY)

    fun getSession(): Flow<LoginResult> {
        return dataStore.data.map {
            LoginResult(
                userId = it[userIdKey] ?: "null",
                name = it[userNameKey] ?: "null",
                token = it[userTokenKey] ?: ""
            )
        }
    }
    suspend fun saveSession(data: LoginResult) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = data.userId
            preferences[userNameKey] = data.name
            preferences[userTokenKey] = data.token
        }
    }

    suspend fun deleteSession() {
        dataStore.edit {
            it.remove(userNameKey)
            it.remove(userIdKey)
            it.remove(userTokenKey)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }


        private const val USER_ID_KEY = "userId"
        private const val USER_NAME_KEY = "name"
        private const val USER_TOKEN_KEY = "token"
    }
}