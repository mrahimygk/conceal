package ir.mrahimy.conceal.util

data class Rgb(
    val r: Int,
    val g: Int,
    val b: Int
)

fun Int.toRgb() = Image.getRgb(this)

fun Int.removeLsBits(howMany: Int) = IntegerUtil.removeLsBits(this)
