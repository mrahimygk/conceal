package ir.mrahimy.conceal.base

import android.app.Application
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import ir.mrahimy.conceal.app.ConcealApplication
import ir.mrahimy.conceal.util.ktx.getColorCompat
import ir.mrahimy.conceal.util.ktx.getColorCompatFromAttr

abstract class BaseAndroidViewModel(
    private val application: Application,
    private val model: BaseModel
) : BaseViewModel(model) {

    protected fun getApplication() = application as ConcealApplication


    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return getApplication().resources.getString(resId, *formatArgs)
    }

    protected fun getColorFromAttr(@AttrRes resId: Int): Int {
        return getApplication().applicationContext.getColorCompatFromAttr(resId)
    }

    protected fun getColor(@ColorRes resId: Int): Int {
        return getApplication().applicationContext.getColorCompat(resId)
    }
}