package ir.mrahimy.conceal.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import ir.mrahimy.conceal.app.ConcealApplication

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    abstract val layoutRes: Int

    val binding by lazy {
        DataBindingUtil.setContentView(this, layoutRes) as DB
    }

    abstract fun configCreationEvents()
    abstract fun configResumeEvents()
    abstract fun bindObservables()
    abstract fun initBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        configCreationEvents()
        bindObservables()
    }

    override fun onResume() {
        super.onResume()
        (application as? ConcealApplication)?.currentActivity = this
        configResumeEvents()
    }
}