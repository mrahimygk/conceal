package ir.mrahimy.conceal.repository

import ir.mrahimy.conceal.net.ApiResult
import ir.mrahimy.conceal.net.api.InfoApi
import ir.mrahimy.conceal.net.safeApiCall

class InfoRepositoryImpl(val api: InfoApi) : InfoRepository {
    override suspend fun putImageInfo(params: Map<String, String>): ApiResult<Any> = safeApiCall {
        return@safeApiCall ApiResult.Success(api.putImageInfo(params))
    }

    override suspend fun putAudioInfo(params: Map<String, String>): ApiResult<Any> = safeApiCall {
        return@safeApiCall ApiResult.Success(api.putAudioInfo(params))
    }
}