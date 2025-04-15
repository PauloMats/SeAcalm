package com.example.seacalm

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.StateFlow

class AuthRepository(context: Context, private val firestore: FirebaseFirestore) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    // Configure Google Sign-in options
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("671046258653-82i7lb244vqetb94t70t37r9s65d1kbr.apps.googleusercontent.com") // Replace with your actual Web Client ID
        .requestEmail()
        .build()
    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
    private val _authState = MutableStateFlow(firebaseAuth.currentUser != null)
    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile

    val authState: StateFlow<Boolean> = _authState

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = auth.currentUser != null
        }
    }

    fun registerWithEmailAndPassword(email: String, password: String): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null) {
                    val userProfile = UserProfile(userId = user.uid, email = email)
                    createUserProfile(userProfile).collect { profileCreated ->
                        result.value = profileCreated
                        _authState.value = profileCreated
                    }
                } else {
                    result.value = false
                    _authState.value = false
                }
            } else {
                result.value = false
                _authState.value = false
            }
        }
        return result
    }

    fun signInWithEmailAndPassword(email: String, password: String): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        getUserProfile(user.uid).collect { profile ->
                            if (profile != null) {
                                _currentUserProfile.value = profile
                                result.value = true
                                _authState.value = true
                            } else {
                                val newUserProfile = UserProfile(userId = user.uid, email = email)
                                createUserProfile(newUserProfile).collect { created ->
                                    if(created){
                                        _currentUserProfile.value = newUserProfile
                                        result.value = true
                                        _authState.value = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return result
    }

    fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

    fun signInWithGoogle(callback: (Intent, (Boolean) -> Unit) -> Unit) {

        val signInIntent = googleSignInClient.signInIntent
        callback(signInIntent) { success ->

            handleSignInResult(success)
        }
    }
/*
    fun handleGoogleSignInResult(task: com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount>, onSuccess: (Boolean) -> Unit) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken, onSuccess)
        } catch (e: ApiException) {
            // Sign in was unsuccessful
            onSuccess(false)
        }
    }
*/
    private fun handleSignInResult(success: Boolean) {
        val user = firebaseAuth.currentUser
        if (success && user != null) {
            getUserProfile(user.uid).collect { profile ->
                if (profile != null) {
                    _currentUserProfile.value = profile
                    _authState.value = true
                } else {
                    val newUserProfile = UserProfile(userId = user.uid, email = user.email ?: "") // Assuming email is available
                    createUserProfile(newUserProfile).collect { created ->
                        if (created) {
                            _currentUserProfile.value = newUserProfile
                            _authState.value = true
                        } else {
                            _authState.value = false // Could not create profile, authState should reflect failure.
                        }
                    }
                }
            }
        } else {
            _authState.value = false
        }
    }
    fun handleGoogleSignInResult(task: com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount>, onSuccess: (Boolean) -> Unit) {
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken, onSuccess)
        } catch (e: ApiException) {
            // Sign in was unsuccessful
            onSuccess(false)
        }
    }
    private fun handleAuthentication(success: Boolean, email: String?) {
        val user = firebaseAuth.currentUser
        if (success && user != null) {

    private fun firebaseAuthWithGoogle(idToken: String?, onSuccess: (Boolean) -> Unit) {
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    onSuccess(task.isSuccessful)
                }
        } else {
            onSuccess(false)
        }
    }
    }

    fun signOutGoogle() {
        googleSignInClient.signOut()
    }

    fun sendPasswordResetEmail(email: String): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                result.value = task.isSuccessful
            }
        return result
    }

    fun getCurrentUser() = firebaseAuth.currentUser

    fun signOut() {
        signOutGoogle()
        firebaseAuth.signOut()
        _authState.value = false
        _currentUserProfile.value = null

    }

    private fun createUserProfile(userProfile: UserProfile): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firestore.collection("users") // You can choose a different collection name
            .document(userProfile.userId)
            .set(userProfile)
            .addOnSuccessListener {
                result.value = true
            }
            .addOnFailureListener {
                // Handle failure, e.g., log the error
                result.value = false
            }
        return result
    }

    fun getUserProfile(userId: String): StateFlow<UserProfile?> {
        val result = MutableStateFlow<UserProfile?>(null)
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userProfile = document.toObject(UserProfile::class.java)
                    result.value = userProfile
                } else {
                    result.value = null
                }
            }
            .addOnFailureListener {
                // Handle failure, e.g., log the error
                result.value = null
            }
        return result
    }
    fun updateUserProfile(userProfile: UserProfile): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firestore.collection("users")
            .document(userProfile.userId)
            .set(userProfile, com.google.firebase.firestore.SetOptions.merge()) // Use merge to allow partial updates
            .addOnSuccessListener {
                result.value = true
                _currentUserProfile.value = userProfile // Update the stored profile
            }
            .addOnFailureListener {
                // Handle failure, e.g., log the error
                result.value = false
            }
        return result
    }


}
