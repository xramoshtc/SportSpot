        package com.example.sportspot.data.local

        import android.content.Context
        import androidx.datastore.preferences.core.edit
        import androidx.datastore.preferences.core.stringPreferencesKey
        import androidx.datastore.preferences.preferencesDataStore
        import kotlinx.coroutines.flow.Flow
        import kotlinx.coroutines.flow.map

        /**
         * Extensió per obtenir una instància de DataStore amb el nom "sportspot_prefs".
         *
         * Aquesta propietat s'afegeix al Context perquè sigui fàcil accedir al DataStore
         * des de qualsevol lloc de l'aplicació.
         *
         * @author Jesús Ramos
         *
         */
        val Context.dataStore by preferencesDataStore("sportspot_prefs")

        /**
         * Classe encarregada de gestionar l'emmagatzematge local de preferències.
         *
         * Actualment s'utilitza per guardar, llegir i esborrar el token d'autenticació.
         *
         * @author Jesús Ramos
         *
         * @param context Context de l'aplicació, necessari per accedir al DataStore.
         */
        class DataStoreManager(private val context: Context) {

            /**
             * Clau per emmagatzemar el token dins del DataStore.
             *
             * @author Jesús Ramos
             *
             * És privada perquè només aquesta classe ha de manipular la clau.
             */
            private val TOKEN_KEY = stringPreferencesKey("token")

            private val ROLE_KEY = stringPreferencesKey("role")


            /**
             * Desa el token rebut al DataStore.
             *
             * Aquesta funció és `suspend` perquè fa una operació d'E/S asíncrona.
             *
             * @author Jesús Ramos
             *
             * @param token Cadena amb el token d'autenticació que volem guardar.
             */
            suspend fun saveToken(token: String) {
                context.dataStore.edit { prefs ->
                    prefs[TOKEN_KEY] = token
                }
            }


            suspend fun saveRole(role: String) {
                context.dataStore.edit { prefs ->
                    prefs[ROLE_KEY] = role
                }
            }


            /**
             * Flux que emet el token actual emmagatzemat o `null` si no n'hi ha.
             *
             * Aquest Flow es pot observar des de ViewModels o altres components per
             * reaccionar quan el token canvia.
             *
             * @author Jesús Ramos
             *
             * @return Flow amb el token actual o `null`.
             */
            val tokenFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[TOKEN_KEY] }


            val roleFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[ROLE_KEY] }


            /**
             * Esborra el token del DataStore.
             *
             * @author Jesús Ramos
             *
             * També és `suspend` perquè modifica les preferències de forma asíncrona.
             */
            suspend fun clearToken() {
                context.dataStore.edit { prefs ->
                    prefs.remove(TOKEN_KEY)
                }
            }

            suspend fun clearRole() {
                context.dataStore.edit { prefs ->
                    prefs.remove(ROLE_KEY)
                }
            }


        }
