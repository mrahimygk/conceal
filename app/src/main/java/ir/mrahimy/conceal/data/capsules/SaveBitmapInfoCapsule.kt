package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap
import ir.mrahimy.conceal.util.ktx.toValidPath
import ir.mrahimy.conceal.util.writeBitmap
import java.io.File
import java.util.*

data class SaveBitmapInfoCapsule(
    val name: String?,
    val time: Date?,
    val bitmap: Bitmap,
    val format: Bitmap.CompressFormat
)

fun SaveBitmapInfoCapsule.save(path: String): String {
    val filePath = path.toValidPath() + "${name}_${time?.time}." +
            format.name.toLowerCase(Locale.ENGLISH)
    File(filePath).writeBitmap(bitmap, format, 100)
    return filePath
}