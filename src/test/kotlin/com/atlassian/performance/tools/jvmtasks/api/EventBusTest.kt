package com.atlassian.performance.tools.jvmtasks.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.function.Consumer

class EventBusTest {

    @Test
    fun shouldSubscribeAndPublish() {
        //given
        class DummyEvent(val payload: String)

        var receivedEvent: DummyEvent? = null
        val callback = Consumer<DummyEvent> { event -> receivedEvent = event }

        //when
        EventBus.subscribe(DummyEvent::class.java, callback)
        EventBus.publish(DummyEvent("Test Event"))

        //then
        assertThat(receivedEvent!!.payload).isEqualTo("Test Event")
    }

    @Test
    fun shouldSubscribeAndPublishMultipleEvents() {
        //given
        class DummyEvent(val payload: String)

        val receivedEvents = mutableListOf<DummyEvent>()
        val callback = Consumer { event: DummyEvent -> receivedEvents.add(event) }

        //when
        EventBus.subscribe(DummyEvent::class.java, callback)
        EventBus.publish(DummyEvent("Test Event 1"))
        EventBus.publish(DummyEvent("Test Event 2"))

        //then
        assertThat(receivedEvents.map { it.payload }).containsExactly("Test Event 1", "Test Event 2")
    }

    @Test
    fun shouldPickUpSubscribersForDifferentEvents() {
        //given
        class DummyEvent1
        class DummyEvent2

        var receivedEvent1: Any? = null
        var receivedEvent2: Any? = null
        val callback1 = Consumer { event: DummyEvent1 -> receivedEvent1 = event }
        val callback2 = Consumer { event: DummyEvent2 -> receivedEvent2 = event }

        //when
        EventBus.subscribe(DummyEvent1::class.java, callback1)
        EventBus.subscribe(DummyEvent2::class.java, callback2)
        EventBus.publish(DummyEvent1())

        //then
        assertThat(receivedEvent1).isNotNull()
        assertThat(receivedEvent2).isNull()
    }

    interface FloorWax
    interface DessertTopping
    class Shimmer: FloorWax, DessertTopping

    @Test
    fun shouldPublishSuperTypes() {
        // given
        var count = 0
        EventBus.subscribe(FloorWax::class.java, Consumer { count++ })

        // when
        EventBus.publishSuperTypes(Shimmer())

        // then
        assertThat(count).isEqualTo(1)
    }
}
