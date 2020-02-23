package ir.mrahimy.conceal.util.ktx

import com.google.gson.Gson
import ir.mrahimy.conceal.net.error.ApiException
import ir.mrahimy.conceal.net.res.ApiResponse

fun Gson.extractData(data: String): String {
    val res = fromJson(data, ApiResponse::class.java)
    if (res.statusCode == 200) {
        return if (res.data == null) "{}" else toJson(res.data)
    } else throw ApiException(res.statusCode)
}