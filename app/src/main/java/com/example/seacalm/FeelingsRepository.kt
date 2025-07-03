package com.example.seacalm

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FeelingsRepository(private val firestore: FirebaseFirestore) {

    fun saveFeelingEntry(feelingEntry: FeelingEntry): StateFlow<Boolean> {
        val result = MutableStateFlow(false)
        firestore.collection("feelings")
            .add(feelingEntry)
            .addOnSuccessListener {
                result.value = true
            }
            .addOnFailureListener { e ->
                // Handle the error, e.g., log it
                result.value = false
            }
        return result
    }
}