package ir.mrahimy.conceal.util.ktx

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat


fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}


fun Context.getColorCompatFromAttr(@AttrRes color: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(color, typedValue, true)
    return typedValue.data
}