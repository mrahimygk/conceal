package ir.mrahimy.conceal.net

import com.google.gson.Gson
import ir.mrahimy.conceal.net.error.ApiException
import ir.mrahimy.conceal.util.ktx.extractData
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.net.UnknownHostException

class ApiInterceptor(
    private val gson: Gson
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val reqBuilder = chain.request().newBuilder()
        try {
            val response = chain.proceed(reqBuilder.build())

            val data = response.body()?.string()?.let {
                gson.extractData(it)
            } ?: ""

            val contentType = response.body()?.contentType()
            val body = ResponseBody.create(contentType, data)

            return response.newBuilder().body(body).build()

        } catch (e: UnknownHostException) {
            throw e
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(-1, e)
        }
    }
}