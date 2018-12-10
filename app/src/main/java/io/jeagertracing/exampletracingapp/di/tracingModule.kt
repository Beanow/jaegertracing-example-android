package io.jeagertracing.exampletracingapp.di

import io.opentracing.Tracer
import io.opentracing.mock.MockTracer
import org.koin.dsl.module.module

val tracingModule = module {

    // This exposes the mock tracker, but cast to Tracer.
    single<Tracer> {
        MockTracer()
    }

}
