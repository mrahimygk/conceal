package ir.mrahimy.conceal.ui.sample

import android.util.Log
import androidx.lifecycle.Observer
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseActivity
import ir.mrahimy.conceal.databinding.ActivitySampleBinding
import kotlinx.android.synthetic.main.activity_sample.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SampleActivity : BaseActivity<SampleViewModel, ActivitySampleBinding>() {

    override val layoutRes = R.layout.activity_sample
    override val viewModel: SampleViewModel by viewModel()
    private val adapter : SampleAdapter by inject()

    override fun configCreationEvents() {
        sample_list.adapter = adapter
    }

    override fun configResumeEvents() {
        //TODO("not implemented")
    }

    override fun bindObservables() {
        viewModel.sampleList.observe(this, Observer {
            it.forEach {
                Log.d("GGG", it.text)
            }
        })
    }

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@SampleActivity
            vm = viewModel
            executePendingBindings()
        }
    }
}