package com.example.advancedconceptsapp.ui.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.advancedconceptsapp.BuildConfig
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ReportingWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val TAG = "ReportingWorker"
        const val USAGE_LOG_COLLECTION = "usage_logs" // Or make this flavor-dependent
    }

    private val db = Firebase.firestore

    override suspend fun doWork(): Result {
        Log.d(TAG, "Worker started: Reporting usage.")

        return try {
            // Example: Add a log entry to a separate Firestore collection
            val logEntry = hashMapOf(
                "timestamp" to System.currentTimeMillis(),
                "message" to "App usage report.",
                "worker_run_id" to id.toString() // Include WorkManager's run ID
            )

            db.collection(BuildConfig.USAGE_LOG_COLLECTION).add(logEntry).await() // Use build config field

            Log.d(TAG, "Worker finished successfully: Log uploaded.")
            Result.success()

        } catch (e: Exception) {
            Log.e(TAG, "Worker failed", e)
            Result.failure() // Or Result.retry() if appropriate
        }
    }
}