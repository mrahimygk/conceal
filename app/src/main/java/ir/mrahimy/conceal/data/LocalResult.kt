package ir.mrahimy.conceal.data

import androidx.annotation.StringRes

sealed class LocalResult<out T : Any> {

    data class Success<out T : Any>(val data: T) : LocalResult<T>()
    data class Error(
        @StringRes val stringRes: Int,
        val errorCode: Int,
        val e: Exception
    ) : LocalResult<Nothing>()
}