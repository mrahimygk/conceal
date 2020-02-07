package ir.mrahimy.conceal.ba

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import ir.mrahimy.conceal.util.ktx.getDrawableCompat

@BindingAdapter("bitmap")
fun ImageView.setBitmap(bitmap: Bitmap?) = bitmap?.let {
    setImageBitmap(bitmap)
}

@BindingAdapter("drawableCompat")
fun ImageView.setDrawableCompat(@DrawableRes resId: Int?) {
    resId?.let {
        setImageDrawable(context.getDrawableCompat(it))
    }
}