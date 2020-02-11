package ir.mrahimy.conceal.ba

import android.view.View
import androidx.databinding.BindingAdapter
import android.view.Gravity
import androidx.annotation.StringRes
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import ir.mrahimy.conceal.R


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