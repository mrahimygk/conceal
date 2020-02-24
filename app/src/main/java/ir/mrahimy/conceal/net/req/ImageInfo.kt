package ir.mrahimy.conceal.net.req

import android.graphics.Bitmap
import java.io.File

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

fun Bitmap.makeImageInfoMap(
    isParsed: Boolean,
    file: File
): Map<String, String> {
    val name = file.name
    val ext = file.extension
    val size = file.length().toString()
    val date = file.lastModified()
    val width = width
    val height = height

    val map = mutableMapOf<String, String>()
    map[NAME_KEY] = name
    map[EXT_KEY] = ext
    map[SIZE_KEY] = size
    map[DATE_KEY] = date.toString()

    map[IS_PARSED_KEY] = isParsed.toString()
    map[WIDTH_KEY] = width.toString()
    map[HEIGHT_KEY] = height.toString()
    return map
}