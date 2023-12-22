package com.atlassian.performance.tools.jvmtasks.api

import net.jcip.annotations.Immutable
import org.apache.logging.log4j.CloseableThreadContext
import org.apache.logging.log4j.ThreadContext // we can implement our own thread stack structure if need be
import java.util.concurrent.Callable

/**
 * @since 1.4.0
 */
@Immutable
object TaskScope {

    fun <T> task(
        name: String,
        call: Callable<T>
    ): T {
        CloseableThreadContext.push(name).use {
            try {
                EventBus.publish(TaskStartedEvent(name))
                val result = call.call()
                EventBus.publish(TaskFinishedEvent(name))
                return result
            } catch (e: Throwable) {
                EventBus.publish(TaskFailedEvent(name, e))
                throw e
            }
        }
    }

    fun stack(): Collection<String> = ThreadContext.getImmutableStack()

    class TaskStartedEvent(val taskName: String)
    class TaskFinishedEvent(val taskName: String)
    class TaskFailedEvent(val taskName: String, val fail: Throwable)

    /**
     * For some reason, internally Kotlin fails to do lambda-SAM conversion. Perhaps it's too old.
     */
    internal fun <T> task(
        name: String,
        call: () -> T
    ): T = task(name, Callable(call))
}
