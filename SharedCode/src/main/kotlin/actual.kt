package ru.mobileup.common

import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun platformName(): String {
    return "Android ${Build.VERSION.RELEASE}"
}

actual fun mainDispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}

actual object Logger {
    actual fun print(tag: String, message: String) {
        Log.d(tag, message)
    }
}