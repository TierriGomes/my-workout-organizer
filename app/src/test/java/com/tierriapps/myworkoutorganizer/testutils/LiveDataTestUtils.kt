package com.tierriapps.myworkoutorganizer.testutils

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: (data: T?) -> Unit = {}
): T? {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object: Observer<T> {
        override fun onChanged(o: T) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke(data)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data
}
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValues(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    duringObserve: (T) -> Unit = {},
    afterObserve: (List<T>) -> Unit = {}
): List<T> {
    val values = mutableListOf<T>()
    val latch = CountDownLatch(1)
    val observer = Observer<T> { value ->
        values.add(value)
        duringObserve(value)
        latch.countDown()
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke(values)

        // Wait for all values to be emitted, respecting the timeout.
        while (!latch.await(time, timeUnit) && values.isNotEmpty()) {
            // Wait until the latch countdowns to zero or all values are emitted.
        }

        return values.toList()

    } finally {
        this.removeObserver(observer)
    }
}