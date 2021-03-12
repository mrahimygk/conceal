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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConcealInputData

        if (!audioDataAsRgbList.contentEquals(other.audioDataAsRgbList)) return false

        return true
    }

    override fun hashCode(): Int {
        return audioDataAsRgbList.contentHashCode()
    }
}