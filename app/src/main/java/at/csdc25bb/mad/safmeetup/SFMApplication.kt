package at.csdc25bb.mad.safmeetup

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SFMApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Started the SFM Application")
        instance = this
    }

    companion object{
        lateinit var instance: SFMApplication
            private set
        const val TAG = "SFMApplication"
    }
}