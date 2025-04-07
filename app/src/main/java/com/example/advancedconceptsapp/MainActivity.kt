package com.example.advancedconceptsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.advancedconceptsapp.ui.TaskScreen
import com.example.advancedconceptsapp.ui.theme.AdvancedConceptsAppTheme
import androidx.work.* // Import WorkManager classes
import com.example.advancedconceptsapp.ui.worker.ReportingWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdvancedConceptsAppTheme { // Use your generated theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskScreen() // Display our TaskScreen
                }
            }
        }
        schedulePeriodicUsageReport(this)
    }

    private fun schedulePeriodicUsageReport(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Only run when connected
            .build()

        val reportingWorkRequest = PeriodicWorkRequestBuilder<ReportingWorker>(
            2, TimeUnit.HOURS // Run roughly every hour (minimum is 15 minutes)
        )
            .setConstraints(constraints)
            .addTag(ReportingWorker.TAG) // Optional tag to find/cancel work later
            .build()

        // Use enqueueUniquePeriodicWork to avoid scheduling duplicates
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ReportingWorker.TAG, // Unique name for this work
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if it's already scheduled
            reportingWorkRequest
        )
        Log.d("MainActivity", "Periodic reporting work scheduled.")
    }
}