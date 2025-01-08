package com.androsh.shopee.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.androsh.shopee.domain.repository.LoginRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import java.security.MessageDigest
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RepositoryLoginImpl(private val context: Context) : LoginRepository {
    private val auth: FirebaseAuth = Firebase.auth


    override suspend fun signInWithGoogle(): FirebaseUser? {
        val googleIdOption: GetGoogleIdOption =
            GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false)
                .setServerClientId("794828671760-hgug7ikopo8bou1s5sppjjpep357ojlu.apps.googleusercontent.com")
                .setAutoSelectEnabled(false).setNonce(createNonceOption()).build()

        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        val credentialManager = CredentialManager.create(context)
        try {

            val result = credentialManager.getCredential(
                context = context, request = request
            )

            val credential = result.credential

            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken, null
                        )
                        return suspendCancellableCoroutine { continuation ->
                            auth.signInWithCredential(firebaseCredential).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    continuation.resume(it.result.user)
                                } else {
                                    continuation.resumeWithException(
                                        it.exception ?: Exception("Login failed")
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.i("GoogleIdTokenParsingException", e.message ?: "")
                    }
                }
            }

        } catch (e: Exception) {
            Log.i("Exception", e.message ?: "")
        }
        return null
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    private fun createNonceOption(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }

    }
}