package ir.mrahimy.conceal.repository

import ir.mrahimy.conceal.net.ApiResult

interface InfoRepository {
    suspend fun putImageInfo(params: Map<String, String>): ApiResult<Any>
    suspend fun putAudioInfo(params: Map<String, String>): ApiResult<Any>
}