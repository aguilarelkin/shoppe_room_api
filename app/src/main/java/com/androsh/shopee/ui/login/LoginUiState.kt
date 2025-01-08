package com.androsh.shopee.ui.login

import com.google.firebase.auth.FirebaseUser

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val user: FirebaseUser? = null,
    val isLogin : Boolean = false
)
