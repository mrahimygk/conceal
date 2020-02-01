package ir.mrahimy.conceal.util

import java.lang.RuntimeException

data class Rgb(
    var r: Int,
    var g: Int,
    var b: Int
)

fun Int.toRgb() = Image.getRgb(this)
fun Rgb.parse() = Image.parseRgb(this)

//TODO: decide by howMany here
fun Int.removeLsBits(howMany: Int) = IntegerUtil.removeLsBits(this)

fun Int.getLsBits(howMany: Int) = when (howMany) {
    3 -> IntegerUtil.get3LsBits(this)
    2 -> IntegerUtil.get2LsBits(this)
    else -> throw RuntimeException("Not implemented YET")
}

/**
 * This method does not respect the sign of concealed number
 */
fun Int.combineBits(other: Int): Int {
    val binA = this.toBinString()
    val binB = this.toBinString()
    val lsbA = binA.drop(6)
    val lsbB = binB.drop(6)
    val whole = lsbA + lsbB
    return whole.toInt(2)
}

fun Int.bitwiseAnd(other: Int) = IntegerUtil.and(this, other)

fun Int.bitwiseOr(other: Int) = IntegerUtil.or(this, other)

fun Int.toBinString(format: String = "%8s") =
    String.format(format, this.toString(2))
        .replace(' ', '0')
//    Integer.toBinaryString(this)