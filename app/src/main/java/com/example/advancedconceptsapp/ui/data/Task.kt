package com.example.advancedconceptsapp.ui.data

import com.google.firebase.firestore.DocumentId // Important for mapping Firestore ID

data class Task(
    @DocumentId // Tells Firestore to map the document ID to this field
    val id: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    // Add a no-argument constructor required by Firestore for deserialization
    constructor() : this("", "", 0L)
}