package ir.mrahimy.conceal.data

data class SeparatedDigits(
    val elementCount: Int,
    val digits: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeparatedDigits

        if (!digits.contentEquals(other.digits)) return false

        return true
    }

    override fun hashCode(): Int {
        return digits.contentHashCode()
    }
}

fun String.toSeparatedDigits(): SeparatedDigits {
    val count = this.length
    val digits = mutableListOf<Int>()
    this.iterator().forEach {
        digits.add(it.toInt())
    }

    return SeparatedDigits(count, digits.toIntArray())
}