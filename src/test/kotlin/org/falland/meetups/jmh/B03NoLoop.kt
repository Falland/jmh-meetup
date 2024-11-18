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

/**
 * What if our problem looks like
 * <pre>{@code
 *  for (item in list) {
 *     //hundreds of lines of code for processing the element
 *  }
 * }</pre>
 *
 * Then we can not count on the loop optimisations: unrolling, inlining and/or vectorisation etc.
 * How do we go about measuring the iteration then?
 */

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
open class B03NoLoop {
    /*
     * But what is the actual problem we are covering with this benchmark?
     * Is it a tight loop? Is it a humongous loop?
     * Is it once in while operation or very hot path of our code?
     *
     * Suppose it is the Problem.kt - huge loop with no-so-often call profile
     */

    @State(Scope.Benchmark)
    open class ExecutionPlan {
        lateinit var arrayList: ArrayList<Int>
        lateinit var linkedList: LinkedList<Int>

        lateinit var arrayIterator: Iterator<Int>
        lateinit var linkedIterator: Iterator<Int>

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
        }
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 10)
    @Measurement(iterations = 5, batchSize = 10)
    fun testIterationLinked10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 100)
    @Measurement(iterations = 5, batchSize = 100)
    fun testIterationLinked100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
    fun testIterationLinked1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.linkedIterator.hasNext())
        blackhole.consume(plan.linkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 10)
    @Measurement(iterations = 5, batchSize = 10)
    fun testIterationArray10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 100)
    @Measurement(iterations = 5, batchSize = 100)
    fun testIterationArray100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 5, batchSize = 1000)
    fun testIterationArray1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.arrayIterator.hasNext())
        blackhole.consume(plan.arrayIterator.next())
    }
}
