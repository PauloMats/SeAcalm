package com.example.seacalm

import com.google.firebase.Timestamp

data class FeelingEntry(
    val userId: String = "",
    val feelingText: String = "",
    val timestamp: Timestamp = Timestamp.now()
)