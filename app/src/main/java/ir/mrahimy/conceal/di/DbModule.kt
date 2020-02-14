package ir.mrahimy.conceal.di

import androidx.room.Room
import ir.mrahimy.conceal.data.db.ConcealDb
import ir.mrahimy.conceal.data.db.migrations.migration1to2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val DB_NAME = "conceal_db"

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ConcealDb::class.java,
            DB_NAME
        )
            .fallbackToDestructiveMigration()
            .addMigrations(
                migration1to2
            )
            .build()
    }

    factory {
        get<ConcealDb>().recordingDao()
    }
}