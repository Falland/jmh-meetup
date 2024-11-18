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
import kotlin.random.Random

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
open class B05Shuffling {
    /*
     * Wait, but are we excluding the regularity in our data set?
     * Is our list populated all at once or there's something running between the adds?
     * Assume it's not all at once
     * Let's shuffle the data, would it change the results?
     */

    @State(Scope.Benchmark)
    open class ExecutionPlan {
        lateinit var shuffledLinkedList: LinkedList<Int>
        lateinit var unshuffledLinkedList: LinkedList<Int>
        lateinit var shuffledArrayList: ArrayList<Int>
        lateinit var unshuffledArrayList: ArrayList<Int>

        var elementsCount: Int = 100_000

        lateinit var shuffledLinkedIterator: Iterator<Int>
        lateinit var shuffledArrayIterator: Iterator<Int>
        lateinit var unshuffledLinkedIterator: Iterator<Int>
        lateinit var unshuffledArrayIterator: Iterator<Int>
        lateinit var baseline: Iterator<Int>

        val rnd: Random = Random(42)
        val set: MutableSet<Any> = HashSet()

        @Setup(Level.Trial)
        fun setup() {
            shuffledLinkedList = LinkedList()
            unshuffledLinkedList = LinkedList()
            shuffledArrayList = ArrayList()
            unshuffledArrayList = ArrayList()

            for (i in 0 until elementsCount) {
                shuffledArrayList.add(allocateRandom())
            }
            for (i in 0 until elementsCount) {
                shuffledLinkedList.add(allocateRandom())
            }
            for (i in 0 until elementsCount) {
                unshuffledArrayList.add(i + rnd.nextInt(1000, 10_000))
            }
            for (i in 0 until elementsCount) {
                unshuffledLinkedList.add(i + rnd.nextInt(1000, 10_000))
            }
//            Collections.shuffle(shuffledLinkedList)
//            Collections.shuffle(shuffledArrayList)

            baseline = Baseline()
            shuffledLinkedIterator = shuffledLinkedList.iterator()
            unshuffledLinkedIterator = unshuffledLinkedList.iterator()
            shuffledArrayIterator = shuffledArrayList.iterator()
            unshuffledArrayIterator = unshuffledArrayList.iterator()
        }

        private fun allocateRandom(): Int {
            val count = rnd.nextInt(42)
            for (i in 0 until count) {
                set.add(Any())
            }
            val toReturn = set.size + rnd.nextInt(1000, 10_000)
            set.clear()
            return toReturn
        }
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 10)
    fun testIterationShuffledLinked10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledLinkedIterator.hasNext())
        blackhole.consume(plan.shuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 100)
    fun testIterationShuffledLinked100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledLinkedIterator.hasNext())
        blackhole.consume(plan.shuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 1000)
    fun testIterationShuffledLinked1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledLinkedIterator.hasNext())
        blackhole.consume(plan.shuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 10)
    fun testIterationShuffledArray10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledArrayIterator.hasNext())
        blackhole.consume(plan.shuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 100)
    fun testIterationShuffledArray100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledArrayIterator.hasNext())
        blackhole.consume(plan.shuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 1000)
    fun testIterationShuffledArray1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.shuffledArrayIterator.hasNext())
        blackhole.consume(plan.shuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 10)
    fun testIterationUnshuffledArray10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledArrayIterator.hasNext())
        blackhole.consume(plan.unshuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 100)
    fun testIterationUnshuffledArray100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledArrayIterator.hasNext())
        blackhole.consume(plan.unshuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 1000)
    fun testIterationUnshuffledArray1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledArrayIterator.hasNext())
        blackhole.consume(plan.unshuffledArrayIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 10)
    fun testIterationUnshuffledLinked10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledLinkedIterator.hasNext())
        blackhole.consume(plan.unshuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 100)
    fun testIterationUnshuffledLinked100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledLinkedIterator.hasNext())
        blackhole.consume(plan.unshuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 1000)
    fun testIterationUnshuffledLinked1000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.unshuffledLinkedIterator.hasNext())
        blackhole.consume(plan.unshuffledLinkedIterator.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 10)
    fun testIterationBaseline10(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 100)
    fun testIterationBaseline100(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 1000)
    @Measurement(iterations = 100, batchSize = 1000)
    fun testIterationBaseline10000(
        blackhole: Blackhole,
        plan: ExecutionPlan,
    ) {
        blackhole.consume(plan.baseline.hasNext())
        blackhole.consume(plan.baseline.next())
    }

    open class Baseline : Iterator<Int> {
        override fun hasNext(): Boolean = true

        override fun next(): Int = 42
    }
}
