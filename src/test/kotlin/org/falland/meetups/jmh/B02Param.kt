package org.falland.meetups.jmh

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import java.util.LinkedList
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
open class B02Param {
    /*
     * Let's start with one of the questions: what is the size of the problem?
     * Suppose the answer it from 10 to a thousand of elements
     * Great, let's maybe see if we can parametrise the benchmark
     */

    @State(Scope.Benchmark)
    open class ExecutionPlan {
        lateinit var arrayList: ArrayList<Int>
        lateinit var linkedList: LinkedList<Int>

        @Param("10", "100", "1000")
        var elementsCount: Int = 0

        @Setup(Level.Trial)
        fun setup() {
            linkedList = LinkedList()
            arrayList = ArrayList()

            for (i in 0 until elementsCount) {
                arrayList.add(i)
            }
            for (i in 0 until elementsCount) {
                linkedList.add(i)
            }
        }
    }

    @Benchmark
    fun testIterationLinked(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        val iterator = plan.linkedList.iterator()
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next())
        }
    }

    @Benchmark
    fun testIterationArray(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        val iterator = plan.arrayList.iterator()
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next())
        }
    }
}
