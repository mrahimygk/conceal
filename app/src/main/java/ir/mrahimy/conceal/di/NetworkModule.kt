package ir.mrahimy.conceal.di

import com.google.gson.GsonBuilder
import ir.mrahimy.conceal.net.AnnotationExclusionStrategy
import ir.mrahimy.conceal.net.ApiInterceptor
import ir.mrahimy.conceal.net.BaseUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

object LogInterceptorQ : Qualifier
object ApiInterceptorQ : Qualifier
object RetrofitServiceQ : Qualifier
object OkHttpServiceQ : Qualifier

val networkModule = module {
    factory<Interceptor>(LogInterceptorQ) {
        HttpLoggingInterceptor { log ->
            Timber.d(log)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory {
        GsonBuilder()
            .setExclusionStrategies(AnnotationExclusionStrategy)
            .disableHtmlEscaping()
            //.registerTypeAdapter(
            //TransactionHistoryRes::class.java,
            //TransactionHistoryDeserializer<TransactionHistoryRes>()
            //)
            .create()
    }

    factory(ApiInterceptorQ) {
        ApiInterceptor(get())
    }

    single<OkHttpClient>(OkHttpServiceQ) {
        OkHttpClient.Builder().apply {
            addInterceptor(get(ApiInterceptorQ))
            addInterceptor(get(LogInterceptorQ))
        }.build()
    }

    single<Retrofit>(RetrofitServiceQ) {
        Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .client(get(OkHttpServiceQ))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}