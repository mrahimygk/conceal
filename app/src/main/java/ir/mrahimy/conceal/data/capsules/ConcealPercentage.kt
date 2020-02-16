package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

data class ConcealPercentage(
    val id: Int,
    val percent: Float,
    val data: Bitmap?,
    val positionOnList: Int,
    val lastWaveArrayIndexChecked: Int,
    val done: Boolean

)

fun ViewModel.empty() = ConcealPercentage(1, 0f, null, 0, 0, false)