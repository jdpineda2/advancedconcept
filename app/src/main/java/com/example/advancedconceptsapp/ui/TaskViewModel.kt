package com.example.advancedconceptsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advancedconceptsapp.ui.data.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.example.advancedconceptsapp.BuildConfig // Import build config fields

// Define collection name constants (we'll use this for flavors later)
object FirestoreCollections {
    const val TASKS = "tasks" // Default
    // We might override this per flavor later
}


class TaskViewModel : ViewModel() {

    private val db = Firebase.firestore
    // TODO: Make collection name configurable via Flavors later
    private val tasksCollection = db.collection(BuildConfig.TASKS_COLLECTION) // Use build config field

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            try {
                // Use snapshots for real-time updates (optional but good practice)
                tasksCollection.orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshots, e ->
                        if (e != null) {
                            _error.value = "Error listening for task updates: ${e.localizedMessage}"
                            return@addSnapshotListener
                        }

                        if (snapshots != null) {
                            _tasks.value = snapshots.toObjects<Task>()
                            _error.value = null // Clear error on success
                        }
                    }
                // --- OR ---
                // One-time fetch:
                // val result = tasksCollection.orderBy("timestamp").get().await()
                // _tasks.value = result.toObjects(Task::class.java)
                // _error.value = null
            } catch (e: Exception) {
                _error.value = "Error loading tasks: ${e.localizedMessage}"
            }
        }
    }

    fun addTask(description: String) {
        if (description.isBlank()) {
            _error.value = "Task description cannot be empty."
            return
        }
        viewModelScope.launch {
            try {
                val task = Task(description = description, timestamp = System.currentTimeMillis())
                tasksCollection.add(task).await() // Let Firestore generate the ID
                _error.value = null // Clear error on success
                // UI will update automatically via the snapshot listener
            } catch (e: Exception) {
                _error.value = "Error adding task: ${e.localizedMessage}"
            }
        }
    }
}