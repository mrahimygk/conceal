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