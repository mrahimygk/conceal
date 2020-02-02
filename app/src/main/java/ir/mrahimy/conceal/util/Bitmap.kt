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

/**
 * @param sampleRate the sample rate of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putSampleRate(sampleRate: Int) : Int {
    val audioSampleRate = sampleRate.toString().toSeparatedDigits()
    val sampleRateElementCount = audioSampleRate.elementCount.toBinString(format = "%4s")

    var position = 0

    var binaryString2BitsChunkStr = sampleRateElementCount.substring(0, 2)
    var binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)

    var data = this[position].r.bitwiseOr(binaryString2BitsChunk)
    this[position].r = data
    position += 1

    binaryString2BitsChunkStr = sampleRateElementCount.substring(2, 4)
    binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)

    data = this[position].r.bitwiseOr(binaryString2BitsChunk)
    this[position].r = data
    position += 1

    repeat(audioSampleRate.elementCount) {
        val element = it.toBinString(format = "%4s")
        binaryString2BitsChunk = element.substring(0, 2).toInt(2)
        this[position].r = this[position].r.bitwiseOr(binaryString2BitsChunk)
        position += 1

        binaryString2BitsChunk = element.substring(2, 4).toInt(2)
        this[position].r = this[position].r.bitwiseOr(binaryString2BitsChunk)
        position += 1
    }

    return position
}

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