package com.example.coffeeshop

import android.app.Application
import android.util.Log
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class App : Application() {

    companion object {
        private const val TAG = "Application"
    }

    override fun onCreate() {
        super.onCreate()

        initializeFirebase()
    }

    private fun initializeFirebase() {
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
                Log.d(TAG, "Firebase initialized successfully")
            }

            setupAppCheck()
            verifyFirebaseConfiguration()

        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
        }
    }

    private fun setupAppCheck() {
        try {
            val firebaseAppCheck = FirebaseAppCheck.getInstance()

            when {
                BuildConfig.DEBUG -> {
                    firebaseAppCheck.installAppCheckProviderFactory(
                        DebugAppCheckProviderFactory.getInstance()
                    )
                    Log.d(TAG, "Debug App Check configured")

                    // Get and log debug token
                    firebaseAppCheck.getAppCheckToken(false).addOnSuccessListener { tokenResult ->
                        Log.d(TAG, "Debug App Check Token: ${tokenResult.token}")
                    }
                }
                else -> {
                    firebaseAppCheck.installAppCheckProviderFactory(
                        PlayIntegrityAppCheckProviderFactory.getInstance()
                    )
                    Log.d(TAG, "Play Integrity App Check configured")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "App Check setup failed", e)
        }
    }

    private fun verifyFirebaseConfiguration() {
        try {
            val options = FirebaseOptions.fromResource(this)
            if (options == null) {
                Log.e(TAG, "Firebase options not found. Check google-services.json")
                return
            }

            Log.d(TAG, "Firebase Project ID: ${options.projectId}")
            Log.d(TAG, "Firebase Application ID: ${options.applicationId}")
            Log.d(TAG, "Firebase Database URL: ${options.databaseUrl}")

        } catch (e: Exception) {
            Log.e(TAG, "Firebase configuration verification failed", e)
        }
    }
}