package ir.mrahimy.conceal.data

data class Waver(
    val data: LongArray,
    val sampleRate: Long
)

fun LongArray.mapToUniformDouble(): DoubleArray {
    val max = max()?.toDouble() ?: return DoubleArray(1)
    return map {
        (it.toDouble() / max)
    }.toDoubleArray()
}

fun DoubleArray.mapToRgbValue(): IntArray {
    return map {
        (it * 255).toInt()
    }.toIntArray()
}