package ir.mrahimy.conceal.util

/*
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun rescaleImage(path: String, width: Int, height: Int): Bitmap {

    val scaleOptions = BitmapFactory.Options()
    scaleOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, scaleOptions)
    var scale = 1
    while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
        scale *= 2
    }

    val outOptions = BitmapFactory.Options()
    outOptions.inSampleSize = scale
    return BitmapFactory.decodeFile(path, outOptions)
}
*/