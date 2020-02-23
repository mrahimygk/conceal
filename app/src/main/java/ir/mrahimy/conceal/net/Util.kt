package ir.mrahimy.conceal.net

import com.crashlytics.android.Crashlytics
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.net.error.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * a func to handle network errors
 * Safely Calls the suspend function inside a co-routine context and returns an error if exception occurs
 */
suspend fun <T : Any> safeApiCall(
    call: suspend () -> ApiResult<T>
): ApiResult<T> {
    return withContext(Dispatchers.Main) {
        try {
            withContext(Dispatchers.IO) {
                call()
            }
        } catch (e: Exception) {
            Crashlytics.logException(e)
            val jsonError = R.string.network_error
            val errorCode = if (e is ApiException) e.statusCode else -1
            ApiResult.Error(jsonError, errorCode)
        }
    }
}
