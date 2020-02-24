package ir.mrahimy.conceal.net.api

import retrofit2.http.POST
import retrofit2.http.QueryMap

interface InfoApi {

    @POST("image.php")
    suspend fun putImageInfo(@QueryMap params: Map<String, String>): Any

    @POST("audio.php")
    suspend fun putAudioInfo(@QueryMap params: Map<String, String>): Any
}