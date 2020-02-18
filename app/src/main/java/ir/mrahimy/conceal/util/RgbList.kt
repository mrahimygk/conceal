package ir.mrahimy.conceal.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.data.LocalResult
import ir.mrahimy.conceal.data.Rgb
import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.data.capsules.ConcealPercentage
import ir.mrahimy.conceal.data.toSeparatedDigits
import ir.mrahimy.conceal.util.ktx.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.random.Random

const val PERCENT_CHECK_MOD = 77

fun List<Rgb>.remove3Lsb(): List<Rgb> = map {
    val r = it.r.removeLsBits(3)
    val g = it.g.removeLsBits(3)
    val b = it.b.removeLsBits(3)
    Rgb(r, g, b)
}.toMutableList()

/**
 * @param waveFile the audio to put it's HEADER info inside lsb of the r layer
 * @returns the position of last injected bit. Used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putWaverHeaderInfo(waveFile: Waver): Int {
    var position = putSampleRate(waveFile.sampleRate.toInt())
    position = putChannelCount(position, waveFile.channelCount)
    position = putFrameCount(position, waveFile.frameCount.toInt())
    position = putValidBits(position, waveFile.validBits)
    return putMaxValue(position, waveFile.maxValue.toInt())
}

/**
 * @param sampleRate the sample rate of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putSampleRate(sampleRate: Int) = putNumber(0, sampleRate)

private fun List<Rgb>.putNumber(startingPosition: Int, number: Int): Int {

    var position = startingPosition
    val separatedDigits = number.toString().toSeparatedDigits()
    val elementCount = separatedDigits.elementCount.toBinString(format = "%4s")
    var binaryString2BitsChunkStr = elementCount.substring(0, 2)
    var binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)
    var data = this[position].r.bitwiseOr(binaryString2BitsChunk)
    this[position].r = data
    position += 1

    binaryString2BitsChunkStr = elementCount.substring(2, 4)
    binaryString2BitsChunk = binaryString2BitsChunkStr.toInt(2)

    data = this[position].r.bitwiseOr(binaryString2BitsChunk)
    this[position].r = data
    position += 1

    separatedDigits.digits.forEach {
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
 * @param channelCount the channel count of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putChannelCount(startingPosition: Int, channelCount: Int) =
    putNumber(startingPosition, channelCount)


/**
 * @param frameCount the frame count of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putFrameCount(startingPosition: Int, frameCount: Int) =
    putNumber(startingPosition, frameCount)


/**
 * @param validBits the frame count of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putValidBits(startingPosition: Int, validBits: Int) =
    putNumber(startingPosition, validBits)


/**
 * @param validBits the frame count of audio to put inside lsb of the r layer
 * @returns the position of last injected bit. maybe used to start inserting audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putMaxValue(startingPosition: Int, validBits: Int) =
    putNumber(startingPosition, validBits)


fun List<Rgb>.getSampleRate(): TwoParts<Int, Int> = getSeparatedNumber(0)
fun List<Rgb>.getChannelCount(startingPosition: Int): TwoParts<Int, Int> =
    getSeparatedNumber(startingPosition)

fun List<Rgb>.getFrameCount(startingPosition: Int): TwoParts<Int, Int> =
    getSeparatedNumber(startingPosition)

fun List<Rgb>.getValidBits(startingPosition: Int): TwoParts<Int, Int> =
    getSeparatedNumber(startingPosition)

fun List<Rgb>.getMaxValue(startingPosition: Int): TwoParts<Int, Int> =
    getSeparatedNumber(startingPosition)


/**
 * @return a pair of integers:
 *      first : the number which has been retrieved
 *      second : the position of ongoing index in the array
 */
fun List<Rgb>.getSeparatedNumber(startingPosition: Int): TwoParts<Int, Int> {
    var position = startingPosition
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

    return TwoParts(digitList.joinToString("").toInt(), position)
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
            Layer.G -> this[position].g = this[position].g.bitwiseOr(binaryString2BitsChunk)
            Layer.B -> this[position].b = this[position].b.bitwiseOr(binaryString2BitsChunk)

        }
        position += 1
    }

    return position
}

/**
 * @param startingPosition maybe the position of the last inserted index for or previous insertion
 * @param array the integer array to be put inside 3lsb of this list
 * @param image the boundaries of our image: we cannot exceed them
 * @returns the position of last injected bit. used to start inserting another audio data
 * (starting with that position itself)
 */
fun List<Rgb>.putAllSignedIntegers(
    startingPosition: Int,
    array: IntArray,
    image: Bitmap,
    job: Job
) = liveData(job + Dispatchers.IO) {

    val resBitmap = toBitmap(image)
    var data = ConcealPercentage(
        1,
        0f,
        resBitmap,
        startingPosition,
        0,
        false
    )
    delay(50)
    emit(LocalResult.Success(data))
    var res = try {
        putAllSignedIntegersInLoop(
            array,
            image,
            startingPosition,
            0,
            Layer.R,
            this,
            data,
            resBitmap
        )
    } catch (e: IndexOutOfBoundsException) {
        emit(LocalResult.Error(R.string.data_exceeds, 1, e))
        null
    }

    if (res?.shouldChangeTheLayer == true)
        res = try {
            putAllSignedIntegersInLoop(
                array,
                image,
                0,
                res.lastIndexOfIntArray,
                Layer.G,
                this,
                data,
                resBitmap
            )
        } catch (e: IndexOutOfBoundsException) {
            emit(LocalResult.Error(R.string.data_exceeds, 1, e))
            null
        }

    if (res?.shouldChangeTheLayer == true)
        res = try {
            putAllSignedIntegersInLoop(
                array,
                image,
                0,
                res.lastIndexOfIntArray,
                Layer.B,
                this,
                data,
                resBitmap
            )
        } catch (e: IndexOutOfBoundsException) {
            emit(LocalResult.Error(R.string.data_exceeds, 1, e))
            null
        }

    delay(50)
    data = ConcealPercentage(
        1,
        100.0f,
        this@putAllSignedIntegers.toBitmap(image),
        res?.lastPositionOfRgbList ?: 0,
        res?.lastIndexOfIntArray ?: 0,
        true
    )
    emit(LocalResult.Success(data))
}

/**
 * @param startingPosition maybe the position of the last inserted index for or previous insertion
 * @param array the integer array to be put inside 3lsb of this list
 * @param image holds the reference of boundaries of our image: we cannot exceed them
 * @param layer
 * @returns the position of last injected bit. used to start inserting another audio data
 * (starting with that position itself)
 */
private suspend fun List<Rgb>.putAllSignedIntegersInLoop(
    array: IntArray,
    image: Bitmap,
    startingPosition: Int,
    lastCheckedIndex: Int,
    layer: Layer,
    liveData: LiveDataScope<LocalResult<ConcealPercentage>>,
    data: ConcealPercentage,
    resBitmap: Bitmap
): LoopHelper {
    var lastIndexOfWaveDataChecked = lastCheckedIndex
    var position = startingPosition
    var percent = array.findPercent(lastIndexOfWaveDataChecked)
    delay(50)
    var emittingData = ConcealPercentage(
        1,
        percent,
//            this.toBitmap(image),
        resBitmap,
        position,
        lastIndexOfWaveDataChecked,
        false
    )
    liveData.emit(LocalResult.Success(emittingData))
    array.forEachIndexed { index, it ->
        if (index <= lastIndexOfWaveDataChecked) {
            /** continues this forEach to the next element */
            return@forEachIndexed
        }

        if (position + 3 > size) {
            /** breaks this for each */
            if (layer == Layer.B) {
                throw HugeFileException(array.findPercent(index).toInt())
            }
            return LoopHelper(lastIndexOfWaveDataChecked, position, true)
        }
        position = putSignedInteger(position, it, layer)
        lastIndexOfWaveDataChecked = index
        if (lastIndexOfWaveDataChecked % PERCENT_CHECK_MOD == 0) {
            percent = array.findPercent(lastIndexOfWaveDataChecked)
            delay(2)
            emittingData = ConcealPercentage(
                1,
                percent,
//                    this.toBitmap(image),
                resBitmap,
                position,
                lastIndexOfWaveDataChecked,
                false
            )
            liveData.emit(LocalResult.Success(emittingData))

            /**
             * 10 percent chance to reset a RGB layer to zero
             */
            if (Random.nextInt(Int.MAX_VALUE) > Int.MAX_VALUE - Int.MAX_VALUE / 10) {
                liveData.emit(
                    LocalResult.Success(
                        ConcealPercentage(
                            1,
                            percent,
//                        this.zeroLayerMutable(Layer.values().random()).toBitmap(image),
                            resBitmap,
                            position,
                            lastIndexOfWaveDataChecked,
                            false
                        )
                    )
                )
            }

            /**
             * 10 percent chance to reset a layer's random pixel to black
             */
            if (Random.nextInt(Int.MAX_VALUE) > Int.MAX_VALUE - Int.MAX_VALUE / 10) {
                liveData.emit(
                    LocalResult.Success(
                        ConcealPercentage(
                            1,
                            percent,
//                        this.zeroRandomMutable(0).toBitmap(image),
                            resBitmap,
                            position,
                            lastIndexOfWaveDataChecked,
                            false
                        )
                    )
                )
            }

            /**
             * 10 percent chance to reset a layer's random pixel to white
             */
            if (Random.nextInt(Int.MAX_VALUE) > Int.MAX_VALUE - Int.MAX_VALUE / 10) {
                liveData.emit(
                    LocalResult.Success(
                        ConcealPercentage(
                            1,
                            percent,
//                        this.zeroRandomMutable(255).toBitmap(image),
                            resBitmap,
                            position,
                            lastIndexOfWaveDataChecked,
                            false
                        )
                    )
                )
            }

            /**
             * 10 percent chance to sort image pixels by layer value
             */
            if (Random.nextInt(Int.MAX_VALUE) > Int.MAX_VALUE - Int.MAX_VALUE / 10) {
                liveData.emit(
                    LocalResult.Success(
                        ConcealPercentage(
                            1,
                            percent,
//                        sortByLayerValue(Layer.values().random()).toBitmap(image),
                            resBitmap,
                            position,
                            lastIndexOfWaveDataChecked,
                            false
                        )
                    )
                )
            }
        }
    }

    return LoopHelper(lastIndexOfWaveDataChecked, position, false)
}

private fun List<Rgb>.zeroLayerMutable(rgbLayer: Layer): List<Rgb> {
    return when (rgbLayer) {
        Layer.R -> this.map { it.copy(r = 0) }.toMutableList()
        Layer.G -> this.map { it.copy(g = 0) }.toMutableList()
        Layer.B -> this.map { it.copy(b = 0) }.toMutableList()
    }
}

private fun List<Rgb>.zeroRandomMutable(i: Int): List<Rgb> {
    val random = Random.nextInt(Int.MAX_VALUE)
    return this.map {
        when {
            random < Int.MAX_VALUE / 100 -> it.copy(r = i)
            random < Int.MAX_VALUE / 90 -> it.copy(g = i)
            random < Int.MAX_VALUE / 80 -> it.copy(b = i)
            random < Int.MAX_VALUE / 70 -> it.copy(r = i, g = i)
            random < Int.MAX_VALUE / 60 -> it.copy(g = i, b = i)
            random < Int.MAX_VALUE / 50 -> it.copy(r = i, b = i)
            else -> it.copy()
        }
    }.toMutableList()
}

private fun List<Rgb>.sortByLayerValue(layer: Layer?): List<Rgb> {
    return when (layer) {
        null -> this.map { it }.sortedBy { it.r }
        Layer.R -> this.map { it }.sortedBy { it.r }
        Layer.G -> this.map { it }.sortedBy { it.g }
        Layer.B -> this.map { it }.sortedBy { it.b }
    }.toMutableList()
}

private fun IntArray.findPercent(
    lastIndexOfWaveDataChecked: Int
) = (lastIndexOfWaveDataChecked.toFloat() / size.toFloat()) * 100.0f

/**
 * @param image is the reference bitmap to build the resulting bitmap upon.
 */
fun List<Rgb>.toBitmap(image: Bitmap): Bitmap {

    val bitmap = Bitmap.createBitmap(image.width, image.height, image.config)

    var x = 0
    var y = 0
    repeat(size) { l ->
        val rgb = get(l)
        bitmap[x, y] = Color.rgb(rgb.r, rgb.g, rgb.b)
        x++
        if (x >= image.width) {
            y++
            x = 0
        }
    }

    return bitmap
}

/**
 * @return an integer: the number which has been retrieved
 */
fun List<Rgb>.getSignedInteger(startingPosition: Int, layer: Layer): Int {
    var position = startingPosition

    return when (layer) {
        Layer.R -> {
            /**
             * >= 4 means the binary is one of 100, 101, 110, 111
             */
            val sign = if (get(position).r.getLsBits(3) >= 4) -1 else 1
            val lsb1 = get(position++).r.getLsBits(2)
            val lsb2 = get(position++).r.getLsBits(2)
            val lsb3 = get(position++).r.getLsBits(2)
            val lsb4 = get(position).r.getLsBits(2)
            lsb1.combineBits(lsb2, lsb3, lsb4) * sign
        }
        Layer.G -> {
            val sign = if (get(position).g.getLsBits(3) >= 4) -1 else 1
            val lsb1 = get(position++).g.getLsBits(2)
            val lsb2 = get(position++).g.getLsBits(2)
            val lsb3 = get(position++).g.getLsBits(2)
            val lsb4 = get(position).g.getLsBits(2)
            lsb1.combineBits(lsb2, lsb3, lsb4) * sign
        }
        Layer.B -> {
            val sign = if (get(position).b.getLsBits(3) >= 4) -1 else 1
            val lsb1 = get(position++).b.getLsBits(2)
            val lsb2 = get(position++).b.getLsBits(2)
            val lsb3 = get(position++).b.getLsBits(2)
            val lsb4 = get(position).b.getLsBits(2)
            lsb1.combineBits(lsb2, lsb3, lsb4) * sign
        }
    }
}

/**
 * @return a list of integers which has been retrieved
 */
fun List<Rgb>.getAllSignedIntegers(startingPosition: Int): List<Int> {
    val list = mutableListOf<Int>()

    var position = startingPosition
    while (position < size - 3) {
        list.add(getSignedInteger(position, Layer.R))
        position += 4
    }

    position = 0
    while (position < size - 3) {
        list.add(getSignedInteger(position, Layer.G))
        position += 4
    }

    position = 0
    while (position < size - 3) {
        list.add(getSignedInteger(position, Layer.B))
        position += 4
    }

    return list
}

/**
 * We decide where to put wave data, on which layer of rgb
 */
enum class Layer {
    R, G, B
}

data class LoopHelper(
    val lastIndexOfIntArray: Int,
    val lastPositionOfRgbList: Int,
    val shouldChangeTheLayer: Boolean
)

class HugeFileException(val index: Int) : IndexOutOfBoundsException()