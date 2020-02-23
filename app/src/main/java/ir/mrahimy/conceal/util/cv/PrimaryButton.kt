package ir.mrahimy.conceal.util.cv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.util.ktx.view.invisible
import ir.mrahimy.conceal.util.ktx.view.visible
import kotlinx.android.synthetic.main.custome_primary_button.view.*

class PrimaryButton @JvmOverloads constructor(
    context: Context?, attributes: AttributeSet? = null, def: Int = 0
) : ConstraintLayout(context, attributes, def) {

    private val layout: ConstraintLayout =
        LayoutInflater.from(context).inflate(
            R.layout.custome_primary_button,
            this,
            true
        ) as ConstraintLayout

    var text: String = ""
        set(value) {
            field = value
            btn_title?.text = text
        }

    var isLoading: Boolean = false
        set(value) {
            field = value
            setState(if (value) State.Loading else State.Idle)
        }

    var isButtonEnabled = true
        set(value) {
            field = value
            layout.isEnabled = value
            btn_title?.isEnabled = value
        }

    private fun setState(state: State) {
        when (state) {
            State.Loading -> {
                progress_bar?.visible()
                btn_title?.invisible()
            }

            State.Idle -> {
                progress_bar?.invisible()
                btn_title?.visible()
            }
        }
    }

    init {
//        layout.background
    }

    enum class State {
        Loading, Idle
    }
}