package com.androsh.shopee.domain.repository

import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    suspend fun signInWithGoogle(): FirebaseUser?
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun signInAnonymously(): FirebaseUser?
    suspend fun signOut()
}