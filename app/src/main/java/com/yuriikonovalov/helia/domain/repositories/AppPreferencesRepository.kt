package com.yuriikonovalov.helia.domain.repositories

import com.yuriikonovalov.helia.domain.valueobjects.AppPreferences
import com.yuriikonovalov.helia.domain.valueobjects.Theme
import kotlinx.coroutines.flow.Flow


interface AppPreferencesRepository {
    val appPreferences: Flow<AppPreferences>
    suspend fun updateThemePreference(theme: Theme)
    suspend fun updateShowOnboarding(show: Boolean)
    suspend fun updateRememberMe(remember: Boolean)
}