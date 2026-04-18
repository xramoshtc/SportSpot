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

         * @param context Context de l'aplicació, necessari per accedir al DataStore.
         */
        class DataStoreManager(private val context: Context) {

            /**
             * Clau per emmagatzemar el token dins del DataStore.
             *
             * És privada perquè només aquesta classe ha de manipular la clau.
             */
            private val TOKEN_KEY = stringPreferencesKey("token")
            /**
             * Clau per emmagatzemar el rol de l'usuari.
             *
             * S'utilitza per saber si l'usuari és ADMIN o CLIENT.
             */
            private val ROLE_KEY = stringPreferencesKey("role")
            /**
             * Clau per emmagatzemar el nom d'usuari.
             *
             * Permet identificar l'usuari actual sense haver de consultar el backend.
             */
            private val USERNAME_KEY = stringPreferencesKey("username")
            /**
             * Desa el nom d'usuari al DataStore.
             *
             * @param username Nom d'usuari que volem guardar.
             */
            suspend fun saveUsername(username: String) {
                context.dataStore.edit { prefs ->
                    prefs[USERNAME_KEY] = username
                }
            }
            /**
             * Flux que emet el nom d'usuari actual o `null` si no n'hi ha.
             *
             * Es pot observar des de ViewModels per reaccionar a canvis.
             */
            val usernameFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[USERNAME_KEY] }
            /**
             * Esborra el nom d'usuari del DataStore.
             *
             * Útil quan l'usuari tanca sessió.
             */
            suspend fun clearUsername() {
                context.dataStore.edit { prefs ->
                    prefs.remove(USERNAME_KEY)
                }
            }

            /**
             * Desa el token rebut al DataStore.
             *
             * Aquesta funció és `suspend` perquè fa una operació d'E/S asíncrona.
             *
             * @param token Cadena amb el token d'autenticació que volem guardar.
             */
            suspend fun saveToken(token: String) {
                context.dataStore.edit { prefs ->
                    prefs[TOKEN_KEY] = token
                }
            }

            /**
             * Desa el rol de l'usuari (ADMIN o CLIENT).
             *
             * S'utilitza per controlar permisos dins de l'aplicació.
             *
             * @param role Rol retornat pel backend.
             */
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
             * @return Flow amb el token actual o `null`.
             */
            val tokenFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[TOKEN_KEY] }

            /**
             * Flux que emet el rol actual de l'usuari.
             *
             * Permet adaptar la UI segons si l'usuari és ADMIN o CLIENT.
             */
            val roleFlow: Flow<String?> = context.dataStore.data
                .map { prefs -> prefs[ROLE_KEY] }


            /**
             * Esborra el token del DataStore.
             *
             * També és `suspend` perquè modifica les preferències de forma asíncrona.
             */
            suspend fun clearToken() {
                context.dataStore.edit { prefs ->
                    prefs.remove(TOKEN_KEY)
                }
            }
            /**
             * Esborra el rol de l'usuari del DataStore.
             *
             * Normalment es crida juntament amb `clearToken()` en el procés de logout.
             */
            suspend fun clearRole() {
                context.dataStore.edit { prefs ->
                    prefs.remove(ROLE_KEY)
                }
            }


        }
