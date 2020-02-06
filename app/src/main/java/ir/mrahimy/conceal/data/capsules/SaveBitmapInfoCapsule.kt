package ir.mrahimy.conceal.data.capsules

import android.graphics.Bitmap
import java.util.*

data class SaveBitmapInfoCapsule(
    val name: String?,
    val time: Date?,
    val bitmap: Bitmap,
    val format: Bitmap.CompressFormat
)