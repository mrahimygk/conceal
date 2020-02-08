package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap

data class ConcealPercentage(
    val id: Int,
    val percent: Float,
    val data: Bitmap?,
    val positionOnList: Int,
    val lastWaveArrayIndexChecked: Int,
    val done: Boolean

)