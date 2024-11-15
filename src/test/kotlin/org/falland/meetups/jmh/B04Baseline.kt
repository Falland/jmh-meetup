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


@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
open class B04Baseline {
    /*
     * Good, but how far are our results from the practically fastest solution?
     * This data would give us some idea on how to measure the two profiles.
     * Is the difference big or small in comparison with the baseline?
     */

    @State(Scope.Benchmark)
    open class ExecutionPlan {
        lateinit var linkedList: LinkedList<Int>
        lateinit var arrayList: ArrayList<Int>

        lateinit var arrayIterator: Iterator<Int>
        lateinit var linkedIterator: Iterator<Int>
        lateinit var baseline: Iterator<Int>

        var elementsCount: Int = 10_000

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


            arrayIterator = arrayList.iterator()
            linkedIterator = linkedList.iterator()
            baseline = Baseline()
        }
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 10)
    fun testIterationLinked10(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 100)
    fun testIterationLinked100(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
    fun testIterationLinked1000(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 10)
    fun testIterationArray10(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 100)
    fun testIterationArray100(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
    fun testIterationArray1000(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 10)
    fun testIterationBaseline10(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 100)
    fun testIterationBaseline100(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
    fun testIterationBaseline1000(blackhole: Blackhole, plan: ExecutionPlan) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    open class Baseline : Iterator<Int> {
        override fun hasNext(): Boolean = true

        override fun next(): Int  = 42

    }
}