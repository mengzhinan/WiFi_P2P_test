package com.duke.baselib

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * author: duke
 * dateTime: 2017-04-05 16:15
 * description: 线程池
 */
class DExecutor private constructor() {
    val internalExecutor: Executor?
        get() = executor

    fun execute(command: Runnable?) {
        executor?.execute(command)
    }

    fun <T> submit(task: Callable<T>): Future<T>? {
        if (executor == null) {
            return null
        }
        return executor!!.submit(task)
    }

    fun shutdown() {
        executor?.shutdown()
    }

    fun shutdownNow() {
        executor?.shutdownNow()
    }

    companion object {
        @Volatile
        private var instance: DExecutor? = null

        @Volatile
        private var executor: ThreadPoolExecutor? = null
        private val sThreadFactory: ThreadFactory =
            object : ThreadFactory {
                private val mCount =
                    AtomicInteger(1)

                override fun newThread(r: Runnable): Thread {
                    return Thread(r, "Thread #" + mCount.getAndIncrement())
                }
            }

        fun get(): DExecutor? {
            if (instance == null) {
                synchronized(DExecutor::class.java) {
                    if (instance == null) {
                        instance = DExecutor()
                        //可变线程的线程池
                        executor =
                            Executors.newCachedThreadPool(sThreadFactory) as ThreadPoolExecutor
                        executor!!.rejectedExecutionHandler =
                            ThreadPoolExecutor.DiscardOldestPolicy()
                        //单线程的线程池
                        //Executors.newSingleThreadExecutor().execute(this);
                    }
                }
            }
            return instance
        }
    }
}