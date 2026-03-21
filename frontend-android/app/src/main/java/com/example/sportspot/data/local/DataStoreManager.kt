        package com.example.sportspot.data.local

        import android.content.Context
        import androidx.datastore.preferences.core.edit
        import androidx.datastore.preferences.core.stringPreferencesKey
        import androidx.datastore.preferences.preferencesDataStore
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.flow.map

        // Extensió del DataStore
        val Context.dataStore by preferencesDataStore("sportspot_prefs")

        class DataStoreManager(private val context: Context) {

            //Clau per guardar token
            private val TOKEN_KEY = stringPreferencesKey("token")

            // Guardar token
            suspend fun saveToken(token: String) {
                context.dataStore.edit { prefs ->
                    prefs[TOKEN_KEY] = token
                }
            }

            // Llegir token
            val tokenFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[TOKEN_KEY] }

            // Esborrar token
            suspend fun clearToken() {
                context.dataStore.edit { prefs ->
                    prefs.remove(TOKEN_KEY)
                }
            }
        }
