package ir.mrahimy.conceal.util

import android.graphics.Bitmap
import java.io.File

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int = 100) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}