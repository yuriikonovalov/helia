package com.yuriikonovalov.helia.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yuriikonovalov.helia.domain.repositories.AppPreferencesRepository
import com.yuriikonovalov.helia.domain.valueobjects.AppPreferences
import com.yuriikonovalov.helia.domain.valueobjects.Theme
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferencesRepository {
    override val appPreferences = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            AppPreferences(
                theme = preferences.theme,
                showOnboarding = preferences.showOnboarding,
                rememberMe = preferences.rememberMe
            )
        }

    override suspend fun updateThemePreference(theme: Theme) {
        try {
            dataStore.edit { preferences ->
                preferences[THEME] = theme.name
            }
        } catch (exception: IOException) {
            Log.e("AppPreferences", "Failed to update 'theme'", exception)
        }
    }

    override suspend fun updateShowOnboarding(show: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[SHOW_ONBOARDING] = show
            }
        } catch (exception: IOException) {
            Log.e("AppPreferences", "Failed to update 'should hide onboarding'", exception)
        }
    }

    override suspend fun updateRememberMe(remember: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[REMEMBER_ME] = remember
            }
        } catch (exception: IOException) {
            Log.e("AppPreferences", "Failed to update 'remember me'", exception)
        }
    }

    private val Preferences.theme
        get() = this[THEME]?.let { Theme.valueOf(it) } ?: Theme.LIGHT
    private val Preferences.showOnboarding
        get() = this[SHOW_ONBOARDING] ?: true

    private val Preferences.rememberMe
        get() = this[REMEMBER_ME] ?: false

    companion object PreferencesKeys {
        private val SHOW_ONBOARDING = booleanPreferencesKey("show_onboarding")
        private val THEME = stringPreferencesKey("theme")
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
    }
}