package com.androsh.shopee.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsh.shopee.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository, private val auth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _authState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            validateUserSession(user)
        } else {
            _authState.value = null
            _uiState.value = _uiState.value.copy(isLogin = false)
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    fun loginWithGoogle(activity: Activity) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                user = null,
                isLogin = false
            )
            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                loginRepository.signInWithGoogle(activity)
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

    fun loginInAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                user = null,
                isLogin = false
            )
            val result: FirebaseUser? = withContext(Dispatchers.IO) {
                loginRepository.signInAnonymously()
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
            _uiState.value = _uiState.value.copy(isLoading = false)
            _authState.value = null
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccess = false,
                errorMessage = null,
                user = null,
                isLogin = false
            )
        }
    }

    private fun validateUserSession(user: FirebaseUser) {
        user.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = user
                _uiState.value = _uiState.value.copy(isLogin = true)
            } else {
                signOut()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
    }
}