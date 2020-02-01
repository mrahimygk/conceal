package ir.mrahimy.conceal.util

import android.graphics.Bitmap

fun Bitmap.getRgb(x: Int, y: Int): Rgb = this.getPixel(x, y).toRgb()

fun Bitmap.getRgbArray(): List<Rgb> {
    val rgb = mutableListOf<Rgb>()
    (0 until height).forEach { y ->
        (0 until width).forEach { x ->
            rgb.add(getRgb(x, y))
        }
    }
    return rgb
}

fun List<Rgb>.remove3Lsb(): List<Rgb> = map {
    val r = it.r.removeLsBits(3)
    val g = it.g.removeLsBits(3)
    val b = it.b.removeLsBits(3)
    Rgb(r, g, b)
}.toMutableList()

fun List<Rgb>.getSampleRate(): Int {
    var position = 0
    val digitCountFirst = get(position++).r.getLsBits(2)
    val digitCountSecond = get(position++).r.getLsBits(2)

    val digitCount = digitCountFirst.combineBits(digitCountSecond)
    var digitList = mutableListOf<Int>()
    repeat(digitCount) {
        val first = get(position++).r.getLsBits(2)
        val second = get(position++).r.getLsBits(2)
        digitList.add(first.combineBits(second))
    }

    return digitList.joinToString().toInt()
//    val r = it.r.removeLsBits(3)
//    val g = it.g.removeLsBits(3)
//    val b = it.b.removeLsBits(3)
//    Rgb(r, g, b)
}