package ir.mrahimy.conceal.ba

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible")
fun View.setIsVisible(boolean: Boolean?) {
    boolean?.let {
        visibility = if (it) View.VISIBLE
        else View.INVISIBLE
    }
}

@BindingAdapter("app:isGone")
fun View.setIsGone(boolean: Boolean?) {
    boolean?.let {
        visibility = if (it) View.GONE
        else View.VISIBLE
    }
}

