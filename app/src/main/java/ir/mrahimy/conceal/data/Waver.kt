package ir.mrahimy.conceal.data

data class Waver(
    val data: LongArray,
    val sampleRate: Long
)

fun LongArray.mapToUniformDouble(): DoubleArray {
    val max = absolute().max()?.toDouble() ?: return DoubleArray(1)
    return map {
        (it.toDouble() / max)
    }.toDoubleArray()
}

fun DoubleArray.mapToRgbValue(): IntArray {
    return map {
        (it * 255).toInt()
    }.toIntArray()
}

fun LongArray.absolute(): LongArray {
    return map {
        if (it < 0) it * -1 else it
    }.toLongArray()
}

fun LongArray.toByteArray(): ByteArray {
    return map { it.toByte() }.toByteArray()
}