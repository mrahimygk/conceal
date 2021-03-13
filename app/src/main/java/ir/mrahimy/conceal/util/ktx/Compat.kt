package ir.mrahimy.conceal.util.ktx

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}


fun Context.getColorCompatFromAttr(@AttrRes color: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(color, typedValue, true)
    return typedValue.data
}

fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableId)
}

/*
fun Context.getDrawableCompatFromAttr(@AttrRes drawable: Int): Drawable? {
    val typedValue = TypedValue()
    theme.resolveAttribute(drawable, typedValue, true)
    val imageResId = typedValue.resourceId
    return getDrawableCompat(imageResId)
}

fun Context.getFontCompatFromAttr(@AttrRes font: Int): Typeface? {
    val typedValue = TypedValue()
    theme.resolveAttribute(font, typedValue, true)
    val fontResource = typedValue.resourceId
    return ResourcesCompat.getFont(this, fontResource)
}


fun Drawable.setTintDrawable(colors: ColorStateList) {
    val d = DrawableCompat.wrap(this).mutate()
    d.let {
        DrawableCompat.setTintList(it, colors)
    }
}
*/
