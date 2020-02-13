package ir.mrahimy.conceal.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

// rescale image. Find the best scale for the height and width given
fun rescaleImage(path: String, width: Int, height: Int): Bitmap {

    val scaleOptions = BitmapFactory.Options()
    scaleOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, scaleOptions)
    var scale = 1
    while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
        scale *= 2
    }

    // decode with the sample size
    val outOptions = BitmapFactory.Options()
    outOptions.inSampleSize = scale
    return BitmapFactory.decodeFile(path, outOptions)
}