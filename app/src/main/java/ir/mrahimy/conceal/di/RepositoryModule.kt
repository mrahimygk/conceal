package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.data.repository.RecordingRepository
import ir.mrahimy.conceal.data.repository.RecordingRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<RecordingRepository> { RecordingRepositoryImpl(get()) }
}