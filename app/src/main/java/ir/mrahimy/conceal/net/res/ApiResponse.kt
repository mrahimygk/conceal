package ir.mrahimy.conceal.net.res

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_txt")
    val statusText: String,
    @SerializedName("data")
    val data: Any?
)