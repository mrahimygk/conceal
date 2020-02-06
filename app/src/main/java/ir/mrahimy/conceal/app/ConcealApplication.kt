package ir.mrahimy.conceal.app

import android.app.Activity
import android.app.Application
import ir.mrahimy.conceal.di.adapterModule
import ir.mrahimy.conceal.di.modelModule
import ir.mrahimy.conceal.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ConcealApplication : Application() {
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ConcealApplication)
            androidLogger(Level.DEBUG)
            modules(
                adapterModule, viewModelModule, modelModule
            )
        }
    }
}