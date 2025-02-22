package com.example.mecateknik

import android.app.Application
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.mecateknik.ui.login.LoginActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 🔥 Initialisation de Firebase
        FirebaseApp.initializeApp(this)
    }
}
