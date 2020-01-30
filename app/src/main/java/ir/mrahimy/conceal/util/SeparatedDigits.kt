package ir.mrahimy.conceal.util

data class SeparatedDigits(
    val elementCount: Int,
    val digits: IntArray
)

fun String.toSeparatedDigits(): SeparatedDigits {
    val count = this.length
    val digits = mutableListOf<Int>()
    var start = 0
    var end = 1

    (0..length).forEach {
        if (start == length) return@forEach
        digits.add(substring(start, end).toInt())
        start += 1
        end += 1
    }

    return SeparatedDigits(count, digits.toIntArray())
}