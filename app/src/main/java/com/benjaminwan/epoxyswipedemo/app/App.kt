package com.benjaminwan.epoxyswipedemo.app

import android.app.Application
import android.util.Log
import com.airbnb.mvrx.Mavericks
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Mavericks.initialize(this)
        initLogger()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .tag("SwipeMenuLayout")
            .logStrategy(LogCatStrategy())
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean = true
        })
    }

    inner class LogCatStrategy : LogStrategy {

        private var last: Int = 0

        override fun log(priority: Int, tag: String?, message: String) {
            Log.println(priority, randomKey() + tag!!, message)
        }

        private fun randomKey(): String {
            var random = (10 * Math.random()).toInt()
            if (random == last) {
                random = (random + 1) % 10
            }
            last = random
            return random.toString()
        }
    }
}