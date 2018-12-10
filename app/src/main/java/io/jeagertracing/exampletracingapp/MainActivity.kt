package io.jeagertracing.exampletracingapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import io.opentracing.Tracer
import io.opentracing.log.Fields
import io.opentracing.mock.MockTracer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    // Use a realistic dependency injection for kotlin.
    private val tracer: Tracer by inject()
    private var mainText: TextView? = null

    private fun onPressTrace() {

        // Create the span on the main thread.
        val span = tracer.buildSpan("onPressTrace").start()
        span.log(mapOf(Fields.MESSAGE to "Press received"))

        // Drop into a background coroutine.
        GlobalScope.launch {

            // Slow down here.
            delay(500L)

            // And wrap up the span.
            span.log(mapOf(Fields.MESSAGE to "Fake slow op done"))
            span.finish()

            // To show this worked, have the counter update.
            updateTracingInfo()
        }
    }

    private fun updateTracingInfo() {

        // Hop back onto the UI thread.
        GlobalScope.launch(Dispatchers.Main) {

            // Cast the tracer to have a look at the internals.
            val mockTracer = tracer as MockTracer
            val finished = mockTracer.finishedSpans()
            mainText?.text = resources.getString(
                R.string.traces_finished,
                finished.size,
                if (finished.size > 0) finished.last().toString() else ""
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android UI things, the no-magic imperative way.
        setContentView(R.layout.activity_main)

        // The text we want to update.
        mainText = findViewById(R.id.main_text)

        // And the button handler.
        findViewById<Button>(R.id.trace_button)
            .setOnClickListener { onPressTrace() }

        // Trigger an update to start at 0 spans.
        updateTracingInfo()
    }

}
