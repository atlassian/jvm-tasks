package com.atlassian.performance.tools.jvmtasks.api

import net.jcip.annotations.GuardedBy
import net.jcip.annotations.ThreadSafe
import java.util.function.Consumer

/**
 * @since 1.4.0
 */
@ThreadSafe
object EventBus {
    private val subscribers = mutableMapOf<Class<*>, MutableList<Consumer<Any>>>()
    private val lock = Object()

    @GuardedBy("lock")
    fun publish(event: Any) = synchronized(lock) {
        val eventType = event::class.java
        val subscribers = subscribers[eventType] ?: emptyList<Consumer<Any>>()
        publish(event, subscribers)
    }

    @GuardedBy("lock")
    fun publishSuperTypes(event: Any) = synchronized(lock) {
        val eventType = event::class.java
        val superTypes = eventType.recursiveSuperTypes() + eventType
        val subscribers = subscribers
            .filterKeys { type -> type in superTypes }
            .values
            .flatten()
        publish(event, subscribers)
    }

    private fun publish(
        event: Any,
        subscribers: List<Consumer<Any>>
    ) {
        subscribers.forEach { it.accept(event) }
    }

    private fun Class<*>.recursiveSuperTypes(): List<Class<*>> {
        val types = (interfaces + superclass).filterNotNull()
        return types + types.flatMap { it.recursiveSuperTypes() }
    }

    @GuardedBy("lock")
    @Suppress("UNCHECKED_CAST")
    fun <T> subscribe(
        eventType: Class<T>,
        subscriber: Consumer<T>
    ) = synchronized(lock) {
        subscribers
            .getOrPut(eventType) { mutableListOf() }
            .add(subscriber as Consumer<Any>)
    }
}
