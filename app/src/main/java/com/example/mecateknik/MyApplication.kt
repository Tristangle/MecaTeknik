package com.example.mecateknik

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialisation globale de Firebase
        FirebaseApp.initializeApp(this)
    }
}
