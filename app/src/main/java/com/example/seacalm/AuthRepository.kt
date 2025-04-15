package com.example.seacalm

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private var firebaseAuth = FirebaseAuth.getInstance()

    val authState: Flow<FirebaseUser?> = flow {
        emit(firebaseAuth.currentUser)
        firebaseAuth.addAuthStateListener { auth ->
            emit(auth.currentUser)
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser?> = try {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        Result.success(firebaseAuth.currentUser)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser?> = try {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
        Result.success(firebaseAuth.currentUser)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Placeholder for Google Sign-In
    suspend fun googleSignIn(): Result<FirebaseUser?> = try {
        // TODO: Implement Google Sign-In
        Result.failure(Exception("Google Sign-In not implemented yet"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Placeholder for Password Reset
    suspend fun resetPassword(email: String): Result<Void?> = try {
        // TODO: Implement Password Reset
        Result.failure(Exception("Password Reset not implemented yet"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun signOut(): Result<Void?> = try {
        firebaseAuth.signOut()
        Result.success(null)
    } catch (e: Exception) {
        Result.failure(e)
    }
}