package com.example.sportspot.ui.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sportspot.data.local.DataStoreManager
import com.example.sportspot.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AdminViewModel(
    private val repository: AuthRepository,
    dataStore: DataStoreManager
) : ViewModel() {

    // Exposem el token com StateFlow
    val token = dataStore.tokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {

                    val dataStore = DataStoreManager(context)
                    val repository = AuthRepository(dataStore)

                    return AdminViewModel(
                        repository = repository,
                        dataStore = dataStore
                    ) as T
                }
            }
        }
    }

    suspend fun logout(): Boolean {
        val currentToken = token.value

        if (currentToken.isNullOrBlank()) {
            return false
        }

        return try {
            repository.logout(currentToken)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}

