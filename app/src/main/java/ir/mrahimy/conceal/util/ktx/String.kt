package ir.mrahimy.conceal.util.ktx

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

fun String.toValidPath(): String {
    return if (this.endsWith('/')) this else "${this}/"
}

fun String.getNameFromPath() = File(this).name.split('.')[0]

fun String.removeEmulatedPath() = replace("/storage/emulated/0/", "")

fun String.loadBitmap(): Bitmap = BitmapFactory.decodeFile(this)