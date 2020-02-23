package ir.mrahimy.conceal.net.req

const val DATE_KEY = "date"
const val NAME_KEY = "name"
const val EXT_KEY = "ext"
const val SIZE_KEY = "size"

private const val IS_PARSED_KEY = "is_parsed"
private const val SAMPLE_RATE_KEY = "sample_rate"
private const val VALID_BITS_KEY = "valid_bits"
private const val CHANNEL_COUNT_KEY = "channel_count"
private const val FRAME_COUNT_KEY = "frame_count"

internal fun makeAudioInfoMap(
    name: String? = null,
    ext: String? = null,
    size: String? = null,
    date: Long? = 0L,
    isParsed: Boolean? = false,
    sampleRate: Int? = 0,
    validBits: Int? = 0,
    channelCount: Int? = 0,
    frameCount: Int? = 0
): Map<String, String> {
    val map = mutableMapOf<String, String>()
    name?.let { map.put(NAME_KEY, it) }
    ext?.let { map.put(EXT_KEY, it) }
    size?.let { map.put(SIZE_KEY, it) }
    date?.let { map.put(DATE_KEY, it.toString()) }

    isParsed?.let { map.put(IS_PARSED_KEY, it.toString()) }
    sampleRate?.let { map.put(SAMPLE_RATE_KEY, it.toString()) }
    validBits?.let { map.put(VALID_BITS_KEY, it.toString()) }
    channelCount?.let { map.put(CHANNEL_COUNT_KEY, it.toString()) }
    frameCount?.let { map.put(FRAME_COUNT_KEY, it.toString()) }

    return map
}