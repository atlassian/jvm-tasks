package com.atlassian.performance.tools.jvmtasks.api

import org.assertj.core.api.Assertions.*
import org.junit.Test
import java.time.Duration

class IdempotentActionTest {

    @Test
    fun shouldRetryDespiteInitialFailure() {
        val lazyServer = LazyServer()
        val backoff = CountingBackoff()
        val action = IdempotentAction("connect to server") { lazyServer.connect() }

        action.retry(
            maxAttempts = 2,
            backoff = backoff
        )

        assertThat(backoff.calls).isEqualTo(1)
    }

    @Test
    fun shouldFailAfterTooManyAttempts() {
        val brokenServer = BrokenServer()
        val action = IdempotentAction("connect to server") { brokenServer.connect() }
        val backoff = CountingBackoff()
        val maxAttempts = 4

        assertThatExceptionOfType(Exception::class.java)
            .isThrownBy {
            action.retry(
                maxAttempts = maxAttempts,
                backoff = backoff
            )
        }.withStackTraceContaining("I'll never be operational - $maxAttempts")
        assertThat(backoff.calls)
            .`as`("Back-off count")
            .isEqualTo(maxAttempts - 1)
    }
}

private class LazyServer {

    private var available = false

    fun connect() {
        try {
            if (!available) {
                throw Exception("Still setting up!")
            }
        } finally {
            available = true
        }
    }
}

private class BrokenServer {
    private var counter = 0
    fun connect() {
        throw Exception("I'll never be operational - ${++counter}")
    }
}

private class CountingBackoff : Backoff {
    var calls = 0

    override fun backOff(attempt: Int): Duration {
        calls++
        return Duration.ZERO
    }
}