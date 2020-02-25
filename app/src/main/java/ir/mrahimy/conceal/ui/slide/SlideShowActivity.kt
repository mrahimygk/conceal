package ir.mrahimy.conceal.ui.slide

import android.content.Intent
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseActivity
import ir.mrahimy.conceal.databinding.ActivitySlideBinding
import ir.mrahimy.conceal.util.arch.EventObsrver
import org.koin.androidx.viewmodel.ext.android.viewModel

class SlideShowActivity : BaseActivity<SlideShowViewModel, ActivitySlideBinding>() {
    override val viewModel: SlideShowViewModel by viewModel()
    override val layoutRes = R.layout.activity_slide

    override fun bindObservables() {
        viewModel.onShare.observe(this, EventObsrver {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, it)
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
        })
    }

    override fun configCreationEvents() {
        intent?.extras?.get("PATH")?.let {
            viewModel.setImagePath(it as String)
        }
    }

    override fun configResumeEvents() {

    }

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@SlideShowActivity
            vm = viewModel
            executePendingBindings()
        }
    }
}