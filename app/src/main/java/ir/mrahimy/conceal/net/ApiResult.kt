package ir.mrahimy.conceal.net

import androidx.annotation.StringRes

sealed class ApiResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(
        @StringRes val stringRes: Int,
        val errorCode: Int
    ) : ApiResult<Nothing>()
}