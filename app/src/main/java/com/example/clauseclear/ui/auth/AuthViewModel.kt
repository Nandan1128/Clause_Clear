package com.example.clauseclear.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clauseclear.data.model.AuthRequest
import com.example.clauseclear.data.model.AuthResponse
import com.example.clauseclear.data.model.RefreshRequest
import com.example.clauseclear.data.repository.AuthRepository
import com.example.clauseclear.utils.UiState
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableLiveData<UiState<AuthResponse>>()
    val authState: LiveData<UiState<AuthResponse>> = _authState

    private val _logoutState = MutableLiveData<UiState<Unit>>()
    val logoutState: LiveData<UiState<Unit>> = _logoutState

    fun login(email: String, password: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response: Response<AuthResponse> = repository.login(AuthRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = UiState.Success(response.body()!!)
                } else {
                    val errorMsg = parseError(response)
                    _authState.value = UiState.Error("Login failed: $errorMsg")
                }
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun register(email: String, password: String) {
        _authState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response: Response<AuthResponse> = repository.register(AuthRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = UiState.Success(response.body()!!)
                } else {
                    val errorMsg = parseError(response)
                    _authState.value = UiState.Error("Registration failed: $errorMsg")
                }
            } catch (e: Exception) {
                _authState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun logout() {
        _logoutState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.logout()
                if (response.isSuccessful) {
                    _logoutState.value = UiState.Success(Unit)
                } else {
                    _logoutState.value = UiState.Error("Logout failed")
                }
            } catch (e: Exception) {
                _logoutState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    // Example usage for refresh to clear warning in repository
    fun refreshToken(token: String) {
        viewModelScope.launch {
            try {
                repository.refresh(RefreshRequest(token))
            } catch (e: Exception) {
                // Handle refresh error
            }
        }
    }

    private fun parseError(response: Response<*>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null) {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", response.message())
            } else {
                response.message()
            }
        } catch (e: Exception) {
            response.message()
        }
    }
}