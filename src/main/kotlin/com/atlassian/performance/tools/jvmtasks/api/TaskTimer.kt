package com.atlassian.performance.tools.jvmtasks.api

import net.jcip.annotations.ThreadSafe
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext
import java.time.Duration
import java.time.Instant.now
import java.util.concurrent.CopyOnWriteArrayList

@Deprecated("Use TaskScope instead")
@ThreadSafe
object TaskTimer {
    private val logger = LogManager.getLogger(this::class.java)
    private val taskStartedHandlers = CopyOnWriteArrayList<TaskStartedHandler>()
    private val taskSucceededHandlers = CopyOnWriteArrayList<TaskSucceededHandler>()
    private val taskFailedHandlers = CopyOnWriteArrayList<TaskFailedHandler>()

    /**
     * Logs the duration taken by [task].
     */
    fun <T> time(
        label: String,
        task: () -> T
    ): T {
        return TaskScope.task(label) {
            val path = ThreadContext.cloneStack().asList()
            taskStartedHandlers.forEach { it.onTaskStarted(path) }
            val start = now()
            val result =
                try {
                    task()
                } catch (e: Exception) {
                    val duration = Duration.between(start, now())
                    taskFailedHandlers.forEach { it.onTaskFailed(path, duration, e) }
                    throw e
                }
            val duration = Duration.between(start, now())
            taskSucceededHandlers.forEach { it.onTaskSucceeded(path, duration) }
            logger.debug("The task took $duration")
            result
        }
    }

    fun subTaskStarted(handler: TaskStartedHandler) {  taskStartedHandlers.add(handler) }
    fun subTaskSucceeded(handler: TaskSucceededHandler) { taskSucceededHandlers.add(handler) }
    fun subTaskFailed(handler: TaskFailedHandler) { taskFailedHandlers.add(handler) }
    fun unsubTaskStarted(handler: TaskStartedHandler) { taskStartedHandlers.remove(handler)}
    fun unsubTaskSucceeded(handler: TaskSucceededHandler) { taskSucceededHandlers.remove(handler) }
    fun unsubTaskFailed(handler: TaskFailedHandler) { taskFailedHandlers.remove(handler) }

    @ThreadSafe
    interface TaskStartedHandler {
        fun onTaskStarted(path: List<String>)
    }

    @ThreadSafe
    interface TaskSucceededHandler {
        fun onTaskSucceeded(path: List<String>, duration: Duration)
    }

    @ThreadSafe
    interface TaskFailedHandler {
        fun onTaskFailed(path: List<String>, duration: Duration, fail: Throwable)
    }
}
