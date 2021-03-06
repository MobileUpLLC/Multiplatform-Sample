package ru.mobileup.common

import kotlinx.coroutines.*
import platform.Foundation.NSLog
import platform.UIKit.UIDevice
import platform.darwin.*
import kotlin.coroutines.CoroutineContext


actual object Logger {
    actual fun print(tag: String, message: String) {
        NSLog("$tag: $message")
    }
}

actual fun platformName(): String {
    return UIDevice.currentDevice.systemName() +
            " " +
            UIDevice.currentDevice.systemVersion
}

actual fun mainDispatcher(): CoroutineDispatcher {
    return MainLoopDispatcher
}


private object UI : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val queue = dispatch_get_main_queue()
        dispatch_async(queue) {
            block.run()
        }
    }
}

//https://github.com/Kotlin/kotlinx.coroutines/issues/470#issuecomment-440080970
@UseExperimental(InternalCoroutinesApi::class)
private object MainLoopDispatcher : CoroutineDispatcher(), Delay {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) {
            try {
                block.run()
            } catch (err: Throwable) {
                Logger.print("UNCAUGHT", err.message ?: "")
                throw err
            }
        }
    }


    @InternalCoroutinesApi
    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, timeMillis * 1_000_000), dispatch_get_main_queue()) {
            try {
                with(continuation) {
                    resumeUndispatched(Unit)
                }
            } catch (err: Throwable) {
                Logger.print("UNCAUGHT", err.message ?: "")
                throw err
            }
        }
    }

    @InternalCoroutinesApi
    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle {
        val handle = object : DisposableHandle {
            var disposed = false
                private set

            override fun dispose() {
                disposed = true
            }
        }
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, timeMillis * 1_000_000), dispatch_get_main_queue()) {
            try {
                if (!handle.disposed) {
                    block.run()
                }
            } catch (err: Throwable) {
                Logger.print("UNCAUGHT", err.message ?: "")
                throw err
            }
        }

        return handle
    }

}