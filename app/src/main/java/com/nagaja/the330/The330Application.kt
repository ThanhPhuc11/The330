package com.nagaja.the330

import android.app.Application
import com.google.firebase.FirebaseApp

class The330Application: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}