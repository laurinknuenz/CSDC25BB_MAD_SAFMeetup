package at.csdc25bb.mad.safmeetup

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SFMApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Started the SFM Application")
    }

    companion object{
        const val TAG = "SFMApplication"
    }
}