package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap
import ir.mrahimy.conceal.data.Rgb
import kotlinx.coroutines.Job

data class ConcealInputData(
    val rgbList: List<Rgb>,
    val position: Int,
    val audioDataAsRgbList: IntArray,
    val refImage: Bitmap,
    val job: Job
)