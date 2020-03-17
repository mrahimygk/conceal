package ir.mrahimy.conceal.app

import android.app.Activity
import android.app.Application
import com.yariksoffice.lingver.Lingver
import ir.mrahimy.conceal.BuildConfig
import ir.mrahimy.conceal.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class ConcealApplication : Application() {
    var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        Lingver.init(this, "en")

        startKoin {
            androidContext(this@ConcealApplication)
            androidLogger(Level.DEBUG)
            modules(
                adapterModule,
                viewModelModule,
                modelModule,
                dbModule,
                repositoryModule,
                networkModule,
                apiModule
            )
        }
    }
}