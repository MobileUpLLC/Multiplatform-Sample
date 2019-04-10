package ru.mobileup.common

import kotlinx.coroutines.*

expect object Logger {
    fun print(tag: String, message: String)
}

expect fun platformName(): String
expect fun mainDispatcher(): CoroutineDispatcher

fun createApplicationScreenMessage(): String {
    return "Kotlin Rocks on ${platformName()}"
}

fun startCountdown(listener: (i: Int) -> Unit): Job {
    return GlobalScope.launch(mainDispatcher()) {
        for (i in 10 downTo 0) {
            Logger.print("Countdown", "$i")
            listener(i)
            delay(1000)
        }
    }
}