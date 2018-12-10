package io.jeagertracing.exampletracingapp

import io.opentracing.Tracer
import io.opentracing.log.Fields
import io.opentracing.mock.MockTracer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MockTraceTests {

    private suspend fun instrumentedSlowOp(tracer: Tracer, howSlow: Long) {
        val span = tracer
            .buildSpan("instrumentedSlowOp $howSlow")
            .start()

        span.setTag("howSlow", howSlow)
        span.log(
            mapOf(
                Fields.MESSAGE to "Slow operation initiated"
            )
        )

        GlobalScope.launch {
            delay(howSlow)
            span.log(
                mapOf(
                    Fields.MESSAGE to "Fake slow op done"
                )
            )
            span.finish()
        }.join()
    }

    @Test
    fun demonstrateAMockedTrace() {
        // Given
        val mockTracer = MockTracer()
        val howSlow = 500L

        // When
        runBlocking {
            instrumentedSlowOp(
                tracer = mockTracer,
                howSlow = howSlow
            )
        }

        // Then
        assertEquals(1, mockTracer.finishedSpans().size)
        
        val span1 = mockTracer.finishedSpans()[0]
        assertEquals("instrumentedSlowOp 500", span1.operationName())
        assertEquals(1, span1.tags().size)
        assertEquals(howSlow, span1.tags()["howSlow"])
    }

}
