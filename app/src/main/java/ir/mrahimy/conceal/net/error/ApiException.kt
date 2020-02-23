package ir.mrahimy.conceal.net.error

import java.io.IOException

class ApiException(val statusCode: Int, e: Throwable? = null) : IOException(e)