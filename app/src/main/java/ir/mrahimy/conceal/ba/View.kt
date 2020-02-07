package ir.mrahimy.conceal.ba

import android.view.View
import androidx.databinding.BindingAdapter
import android.view.Gravity
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

@BindingAdapter("app:tooltip")
fun View.setTooltip(hasTooltip: Boolean?) {
    val tooltip = SimpleTooltip.Builder(this.context)
        .anchorView(this)
        .text(context.getString(R.string.hold_to_start_recording_tooltip))
        .gravity(Gravity.TOP)
        .animated(true)
        .transparentOverlay(false)
        .build()

    hasTooltip?.let {
        if (it) tooltip.show()
        else tooltip.dismiss()
    }
}