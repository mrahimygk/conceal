package ir.mrahimy.conceal.ba

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bitmap")
fun ImageView.setBitmap(bitmap: Bitmap?) {
    bitmap?.let {
        setImageBitmap(bitmap)
    }
}