package ir.mrahimy.conceal.net.req

import ir.mrahimy.conceal.data.Waver
import java.io.File

const val DATE_KEY = "date"
const val NAME_KEY = "name"
const val EXT_KEY = "ext"
const val SIZE_KEY = "size"

private const val IS_PARSED_KEY = "is_parsed"
private const val SAMPLE_RATE_KEY = "sample_rate"
private const val VALID_BITS_KEY = "valid_bits"
private const val CHANNEL_COUNT_KEY = "channel_count"
private const val FRAME_COUNT_KEY = "frame_count"

fun Waver.makeAudioInfoMap(
    isParsed: Boolean,
    file: File
): Map<String, String> {
    val name = file.name
    val ext = file.extension
    val size = file.length().toString()
    val date = file.lastModified()
    val sampleRate = sampleRate
    val validBits = validBits
    val channelCount = channelCount
    val frameCount = frameCount

    val map = mutableMapOf<String, String>()
    map[NAME_KEY] = name
    map[EXT_KEY] = ext
    map[SIZE_KEY] = size
    map[DATE_KEY] = date.toString()

    map[IS_PARSED_KEY] = isParsed.toString()
    map[SAMPLE_RATE_KEY] = sampleRate.toString()
    map[VALID_BITS_KEY] = validBits.toString()
    map[CHANNEL_COUNT_KEY] = channelCount.toString()
    map[FRAME_COUNT_KEY] = frameCount.toString()

    return map
}