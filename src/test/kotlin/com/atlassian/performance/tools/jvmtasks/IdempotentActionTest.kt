package com.atlassian.performance.tools.jvmtasks

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.time.Duration

class IdempotentActionTest {

    private val lazyServer = LazyServer()
    private val brokenServer = BrokenServer()

    @Test
    fun shouldRetryDespiteInitialFailure() {
        val backoff = CountingBackoff()
        val action = IdempotentAction("connect to server") { lazyServer.connect() }

        action.retry(
            maxAttempts = 2,
            backoff = backoff
        )

        assertThat(backoff.calls, equalTo(1))
    }

    @Test(expected = Exception::class)
    fun shouldFailAfterTooManyAttempts() {
        val action = IdempotentAction("connect to server") { brokenServer.connect() }

        action.retry(
            maxAttempts = 4,
            backoff = CountingBackoff()
        )
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

    fun connect() {
        throw Exception("I'll never be operational")
    }
}

private class CountingBackoff : Backoff {
    var calls = 0

    override fun backOff(attempt: Int): Duration {
        calls++
        return Duration.ZERO
    }
}