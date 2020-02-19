package ir.mrahimy.conceal.ba

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import ir.mrahimy.conceal.R
import android.view.animation.TranslateAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator


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

@BindingAdapter("tooltip")
fun View.setTooltip(@StringRes tooltip: Int?) {
    val tooltipView = SimpleTooltip.Builder(this.context)
        .anchorView(this)
        .text(context.getString(tooltip ?: R.string.click_to_open_file))
        .gravity(Gravity.TOP)
        .animated(true)
        .transparentOverlay(false)
        .padding(32f)
        .build()

    if (tooltip != null) tooltipView.show()
    else tooltipView.dismiss()

}

@BindingAdapter("android:layout_marginBottom")
fun View.setBottomMargin(bottomMargin: Float) {
    val mLayoutParams = layoutParams as MarginLayoutParams
    mLayoutParams.setMargins(
        mLayoutParams.leftMargin, mLayoutParams.topMargin,
        mLayoutParams.rightMargin, Math.round(bottomMargin)
    )
    layoutParams = mLayoutParams
}