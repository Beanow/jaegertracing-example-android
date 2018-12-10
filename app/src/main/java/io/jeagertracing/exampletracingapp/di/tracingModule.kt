package io.jeagertracing.exampletracingapp.di

import io.jaegertracing.Configuration
import io.opentracing.Tracer
import org.koin.dsl.module.module

val tracingModule = module {

    // Note: createOnStart plus startKoin in a launch
    // forces the tracer to be initialized off the main thread.
    // This prevents problems with NetworkOnMainThreadException.
    single<Tracer>(createOnStart = true) {
        Configuration("ExampleTracingService")
            .withSampler(
                Configuration.SamplerConfiguration()
                    .withType("const")
                    .withParam(1)
            )
            .withReporter(
                Configuration.ReporterConfiguration()
                    .withLogSpans(true)
                    .withSender(
                        Configuration.SenderConfiguration()
                            .withEndpoint("https://jaeger-collector.example.org/api/traces")
                    )
            )
            .tracer
        // Alternative way to prevent NetworkOnMainThreadException, provide these ourselves.
//            .tracerBuilder
//            .withTag(TRACER_HOSTNAME_TAG_KEY, "dummy-host")
//            .withTag(TRACER_IP_TAG_KEY, "0.0.0.0")
//            .build()
    }

}
