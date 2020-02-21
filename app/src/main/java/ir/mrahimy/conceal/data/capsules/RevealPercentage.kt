package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import ir.mrahimy.conceal.data.Waver

data class RevealPercentage(
    val id: Int,
    val percent: Float,
    val bitmap: Bitmap?,
    val waver: Waver?,
    val positionOnList: Int,
    val lastWaveArrayIndexChecked: Int,
    val done: Boolean

)

fun Any.emptyRevealPercentage() =
    RevealPercentage(
        2,
        0f,
        null,
        null,
        0,
        0,
        false
    )