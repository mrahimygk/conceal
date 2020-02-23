package ir.mrahimy.conceal.net.req

private const val WIDTH_KEY = "width"
private const val HEIGHT_KEY = "height"

internal fun makeImageInfoMap(
    name: String? = null,
    ext: String? = null,
    size: String? = null,
    w: Int? = 0,
    h: Int? = 0,
    date: Long? = 0L
): Map<String, String> {
    val map = mutableMapOf<String, String>()
    name?.let { map.put(NAME_KEY, name) }
    ext?.let { map.put(EXT_KEY, ext) }
    size?.let { map.put(SIZE_KEY, size) }
    date?.let { map.put(DATE_KEY, date.toString()) }
    w?.let { map.put(WIDTH_KEY, w.toString()) }
    h?.let { map.put(HEIGHT_KEY, h.toString()) }
    return map
}