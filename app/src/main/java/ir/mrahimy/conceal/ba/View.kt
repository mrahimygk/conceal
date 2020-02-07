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

@BindingAdapter(
    "tooltip", "tooltipText",
    "isTooltipAnimated", "transparentOverlay", requireAll = false
)
fun View.setTooltip(
    hasTooltip: Boolean?, @StringRes tooltipText: Int?,
    isAnimated: Boolean?, transparentOverlay: Boolean?
) {
    val tooltip = SimpleTooltip.Builder(this.context)
        .anchorView(this)
        .text(context.getString(tooltipText ?: R.string.hold_to_start_recording_tooltip))
        .gravity(Gravity.TOP)
        .animated(isAnimated ?: false)
        .transparentOverlay(transparentOverlay ?: true)
        .build()

    hasTooltip?.let {
        if (it) tooltip.show()
        else tooltip.dismiss()
    }
}