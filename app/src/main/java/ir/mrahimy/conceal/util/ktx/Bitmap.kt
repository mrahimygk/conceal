package ir.mrahimy.conceal.util.ktx

import android.graphics.Bitmap
import ir.mrahimy.conceal.data.Rgb

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
