package com.atlassian.performance.tools.jvmtasks.api

import com.atlassian.performance.tools.jvmtasks.api.TaskTimer.time
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration

class TaskTimerTest {
    private val handler = DummyEventHandler()

    @Before
    fun setUp() {
        TaskTimer.subTaskStarted(handler)
        TaskTimer.subTaskSucceeded(handler)
        TaskTimer.subTaskFailed(handler)
    }

    @After
    fun tearDown() {
        TaskTimer.unsubTaskStarted(handler)
        TaskTimer.unsubTaskSucceeded(handler)
        TaskTimer.unsubTaskFailed(handler)
    }

    @Test
    fun shouldReceiveEvent() {
        time("Success") {
        }

        assertThat(handler.taskStartedCalls).containsOnly(listOf("Success"))
        assertThat(handler.taskSucceededCalls).containsOnly(listOf("Success"))
        assertThat(handler.taskFailedCalls).isEmpty()
    }

    @Test
    fun shouldReceiveEventWhenTaskFails() {
        val exception = catchThrowable {
            time("Fail") {
                throw Exception("test exception")
            }
        }

        assertThat(exception).isNotNull()
        assertThat(handler.taskStartedCalls).containsOnly(listOf("Fail"))
        assertThat(handler.taskFailedCalls).containsOnly(listOf("Fail"))
        assertThat(handler.taskSucceededCalls).isEmpty()
    }

    class DummyEventHandler : TaskTimer.TaskStartedHandler, TaskTimer.TaskSucceededHandler, TaskTimer.TaskFailedHandler {
        val taskStartedCalls = mutableListOf<List<String>>()
        val taskSucceededCalls = mutableListOf<List<String>>()
        val taskFailedCalls = mutableListOf<List<String>>()

        override fun onTaskStarted(path: List<String>) { taskStartedCalls.add(path) }
        override fun onTaskSucceeded(path: List<String>, duration: Duration) { taskSucceededCalls.add(path) }
        override fun onTaskFailed(path: List<String>, duration: Duration, fail: Throwable) { taskFailedCalls.add(path) }
    }
}
