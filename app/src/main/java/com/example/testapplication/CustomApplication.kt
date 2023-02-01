package com.example.testapplication

import android.app.Application
import io.branch.referral.Branch

class CustomApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        // Branch logging for debugging
        Branch.enableTestMode()

        // Branch object initialization
        Branch.getAutoInstance(this)
    }
}