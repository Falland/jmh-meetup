package org.falland.meetups.jmh

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
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
open class B01Naive {
    /*
    * Suppose you've asked to review the benchmark and the result it yields before they are shared with the team
    * How would you go about this benchmark?
    * Would you ask any questions?
    */

    @State(Scope.Benchmark)
    open class ExecutionPlan {
        lateinit var arrayList: ArrayList<Int>
        lateinit var linkedList: LinkedList<Int>

        var elementsCount: Int = 10

        @Setup(Level.Trial)
        fun setup() {
            arrayList = ArrayList()
            linkedList = LinkedList()

            for (i in 0 until elementsCount) {
                arrayList.add(i)
            }
            for (i in 0 until elementsCount) {
                linkedList.add(i)
            }

        }
    }

    @Benchmark
    fun testIterationArray(blackhole: Blackhole, plan: ExecutionPlan) {
        val iterator = plan.arrayList.iterator()
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next())
        }
    }

    @Benchmark
    fun testIterationLinked(blackhole: Blackhole, plan: ExecutionPlan) {
        val iterator = plan.linkedList.iterator()
        while (iterator.hasNext()) {
            blackhole.consume(iterator.next())
        }
    }
}