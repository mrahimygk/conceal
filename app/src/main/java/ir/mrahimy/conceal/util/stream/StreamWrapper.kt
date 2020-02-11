package ir.mrahimy.conceal.util.stream

import java.io.InputStream

fun InputStream.toByteArray() = StreamUtils.convertStreamToByteArray(this)