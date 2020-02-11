package ir.mrahimy.conceal.util.ktx

import ir.mrahimy.conceal.data.Rgb
import ir.mrahimy.conceal.util.lowlevel.LowLevelIntOperations
import ir.mrahimy.conceal.util.lowlevel.LowLevelRgbOperations

fun Int.toRgb() = LowLevelRgbOperations.getRgb(this)
fun Rgb.parse() = LowLevelRgbOperations.parseRgb(this)

//TODO: decide by howMany here
fun Int.removeLsBits(howMany: Int) = LowLevelIntOperations.removeLsBits(this)

fun Int.getLsBits(howMany: Int) = when (howMany) {
    3 -> LowLevelIntOperations.get3LsBits(this)
    2 -> LowLevelIntOperations.get2LsBits(this)
    else -> throw RuntimeException("Not implemented YET")
}

/**
 * This method does not respect the sign of concealed number
 */
fun Int.combineBits(vararg others: Int): Int {
    val binA = this.toBinString()
    val lsbA = binA.drop(6)
    val builder = StringBuilder().apply { append(lsbA) }
    others.forEach {
        val binB = it.toBinString()
        val lsbB = binB.drop(6)
        builder.append(lsbB)
    }
    return builder.toString().toInt(2)
}

fun Int.bitwiseAnd(other: Int) = LowLevelIntOperations.and(this, other)

fun Int.bitwiseOr(other: Int) = LowLevelIntOperations.or(this, other)

fun Int.toBinString(format: String = "%8s") =
    String.format(format, this.toString(2))
        .replace(' ', '0')
//    Integer.toBinaryString(this)