package com.burbon13.template.core

import android.app.Application
import android.content.Context


/**
 * Class which provides global access to the Application context.
 * Specified as the android:name attribute for the application inside AndroidManifest.xml.
 */
class MyApp : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: MyApp? = null
            private set
        val context: Context?
            get() = instance
    }
}
