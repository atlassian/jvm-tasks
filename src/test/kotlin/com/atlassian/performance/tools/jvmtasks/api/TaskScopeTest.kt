package com.atlassian.performance.tools.jvmtasks.api

import com.atlassian.performance.tools.jvmtasks.api.TaskScope.TaskFinishedEvent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.function.Consumer

class TaskScopeTest {
    @Test
    fun shouldGetStack() {
        // given
        var stack1: Collection<String>? = null
        var stack2: Collection<String>? = null
        // when
        val result = TaskScope.task("parent") {
            stack1 = TaskScope.stack()
            TaskScope.task("subtask") {
                stack2 = TaskScope.stack()
                1234
            }
        }
        // then
        assertThat(result).isEqualTo(1234)
        assertThat(stack1).containsExactly("parent")
        assertThat(stack2).containsExactly("parent", "subtask")
    }

    @Test
    fun shouldPublishTaskFinish() {
        // given
        var event: TaskFinishedEvent? = null
        val subscriber = Consumer { finish: TaskFinishedEvent -> event = finish }
        // when
        EventBus.subscribe(TaskFinishedEvent::class.java, subscriber)
        TaskScope.task("foo") {
            "done and done"
        }
        // then
        assertThat(event).isNotNull()
        assertThat(event!!.taskName).isEqualTo("foo")
    }
}