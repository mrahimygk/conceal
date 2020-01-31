package ir.mrahimy.conceal.util

data class Rgb(
    var r: Int,
    var g: Int,
    var b: Int
)

fun Int.toRgb() = Image.getRgb(this)

fun Int.removeLsBits(howMany: Int) = IntegerUtil.removeLsBits(this)

fun Int.bitwiseAnd(other: Int) = IntegerUtil.and(this, other)

fun Int.bitwiseOr(other: Int) = IntegerUtil.or(this, other)

fun Int.toBinString(format: String = "%8s") =
    String.format(format, this.toString(2))
        .replace(' ', '0')
//    Integer.toBinaryString(this)