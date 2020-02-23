package ir.mrahimy.conceal.net.api

import retrofit2.http.POST
import retrofit2.http.QueryMap

interface InfoApi {

    @POST("image/")
    suspend fun putImageInfo(@QueryMap params: Map<String, String>): Any

    @POST("audio/")
    suspend fun putAudioInfo(@QueryMap params: Map<String, String>): Any
}