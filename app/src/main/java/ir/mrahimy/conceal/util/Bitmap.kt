package ir.mrahimy.conceal.util

import android.graphics.Bitmap
import kotlin.math.absoluteValue

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
fun List<Rgb>.putSampleRate(sampleRate: Int): Int {
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

    audioSampleRate.digits.forEach {
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

/**
 * @return a pair on integers:
 *      first : the number which has been retrieved
 *      second : the position of ongoing index in the array
 */
fun List<Rgb>.getSampleRate(): Pair<Int, Int> {
    var position = 0
    val digitCountFirst = get(position++).r.getLsBits(2)
    val digitCountSecond = get(position++).r.getLsBits(2)

    val digitCount = digitCountFirst.combineBits(digitCountSecond)
    val digitList = mutableListOf<Int>()
    repeat(digitCount) {
        val leftNibble = get(position++).r.getLsBits(2)
        val rightNibble = get(position++).r.getLsBits(2)
        val digit = leftNibble.combineBits(rightNibble)
        digitList.add(digit)
    }

    return Pair(digitList.joinToString("").toInt(), position)
}

/**
 * @param startingPosition maybe the position of the last inserted index for sampleRate
 * @returns the position of last injected bit. used to start inserting another audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putSignedInteger(startingPosition: Int, value: Int, layer: Layer): Int {
    var position = startingPosition
    val data = if (value < 0) {
        when (layer) {
            Layer.R -> this[position].r = this[position].r.bitwiseOr(4)
            Layer.G -> this[position].g = this[position].g.bitwiseOr(4)
            Layer.B -> this[position].b = this[position].b.bitwiseOr(4)
        }
        value.absoluteValue
    } else value

    val element = data.toBinString()

    repeat(4) {
        val binaryString2BitsChunk = element.substring(it * 2, it * 2 + 2).toInt(2)
        when (layer) {
            Layer.R -> this[position].r = this[position].r.bitwiseOr(binaryString2BitsChunk)
            Layer.G -> this[position].g = this[position].r.bitwiseOr(binaryString2BitsChunk)
            Layer.B -> this[position].b = this[position].r.bitwiseOr(binaryString2BitsChunk)

        }
        position += 1
    }

    return position
}

/**
 * @param startingPosition maybe the position of the last inserted index for or previous insertion
 * @param array the integer array to be put inside 3lsb of this list
 * @returns the position of last injected bit. used to start inserting another audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putAllSignedIntegers(
    startingPosition: Int,
    array: IntArray,
    imageW: Int,
    imageH: Int
): Int {
    var position = startingPosition
    var shouldChangeTheLayer = false
    var lastIndexOfWaveDataChecked = 0
    var dataExceedsTheContainer = false

    array.forEach {
        if (position + 3 > (imageW - 1) * (imageH - 1)) {
            shouldChangeTheLayer = true
            /** breaks this for each*/
            return@forEach
        }
        position = putSignedInteger(position, it)
    }
    return position
}

enum class Layer {
    R, G, B
}