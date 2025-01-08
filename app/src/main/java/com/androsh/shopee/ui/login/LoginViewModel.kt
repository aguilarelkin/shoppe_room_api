package com.androsh.shopee.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsh.shopee.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                user = null,
                isLogin = false
            )
            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                loginRepository.signInWithGoogle()
            }
            if (result != null) {
                _uiState.value =
                    _uiState.value.copy(isSuccess = true, isLoading = false, user = result)
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ocurrió un error al intentar iniciar sesión. Por favor, inténtalo de nuevo.",
                    isLoading = false
                )
            }
        }
    }

    fun isLoginGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                loginRepository.getCurrentUser()
            }
            if (result != null) {
                _uiState.value =
                    _uiState.value.copy(isLogin = true, isLoading = false, user = result)
            } else {
                _uiState.value = _uiState.value.copy(isLogin = false, isLoading = false)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                user = null,
                isLogin = false
            )
            loginRepository.signOut()
        }
    }
}