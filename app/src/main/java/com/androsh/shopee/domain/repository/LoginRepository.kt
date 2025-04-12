package com.androsh.shopee.domain.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    suspend fun signInWithGoogle(activity: Activity): FirebaseUser?
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun signInAnonymously(): FirebaseUser?
    suspend fun signOut()
}