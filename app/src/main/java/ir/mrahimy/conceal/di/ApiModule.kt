package ir.mrahimy.conceal.di

import ir.mrahimy.conceal.net.api.InfoApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val apiModule = module {
    factory<InfoApi> { get<Retrofit>(RetrofitServiceQ).create() }
}