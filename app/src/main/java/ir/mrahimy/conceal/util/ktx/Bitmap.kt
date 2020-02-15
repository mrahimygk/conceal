package ir.mrahimy.conceal.util.ktx

import android.graphics.Bitmap
import ir.mrahimy.conceal.data.Rgb
import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.util.*

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

fun Bitmap.parseWaver(): Waver {
    val list = getRgbArray()
    val parsedSampleRate = list.getSampleRate()
    val parsedChannelCount = list.getChannelCount(parsedSampleRate.position)
    val parsedFrameCount = list.getFrameCount(parsedChannelCount.position)
    val parsedValidBits = list.getValidBits(parsedFrameCount.position)
    val parsedMaxValue = list.getMaxValue(parsedValidBits.position)

    val parsedWaveData =
        list.getAllSignedIntegers(parsedMaxValue.position)
            .map { n -> n.toDouble() / 255.0 }
            .map { n -> n * parsedMaxValue.number }
            .map { n -> n.toLong() }
            .toLongArray()

    return Waver(
        parsedWaveData,
        parsedSampleRate.number.toLong(),
        parsedChannelCount.number,
        parsedFrameCount.number.toLong(),
        parsedValidBits.number
    )
}