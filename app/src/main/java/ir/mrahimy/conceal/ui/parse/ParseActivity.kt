package ir.mrahimy.conceal.ui.parse

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.google.android.material.snackbar.Snackbar
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseActivity
import ir.mrahimy.conceal.enums.ChooserType
import ir.mrahimy.conceal.util.EventObsrver
import ir.mrahimy.conceal.util.putAllSignedIntegers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import ir.mrahimy.conceal.data.MediaState
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.databinding.ActivityParseBinding
import kotlinx.android.synthetic.main.activity_main.visualizer_view
import kotlinx.android.synthetic.main.activity_parse.*

private const val PICK_IMAGE = 1000

@RuntimePermissions
class ParseActivity : BaseActivity<ParseActivityViewModel, ActivityParseBinding>() {

    override val layoutRes = R.layout.activity_parse
    override val viewModel: ParseActivityViewModel by viewModel()

    private var audioVisualization: AudioVisualization? = null

    private var mediaPlayer: MediaPlayer? = null

    override fun bindObservables() {
        viewModel.onChooseImage.observe(this, EventObsrver {
            chooseMediaWithPermissionCheck(
                ChooserType.Image,
                getString(R.string.select_image_title),
                PICK_IMAGE
            )
        })

        viewModel.snackMessage.observe(this, EventObsrver {
            Snackbar.make(root_view, it, Snackbar.LENGTH_LONG).show()
        })

        viewModel.onStopPlaying.observe(this, EventObsrver {
            stopPlaying()
        })

        viewModel.onPlayOutputAudio.observe(this, EventObsrver {
            if (it == "stop") stopPlaying()
            else play(it.toUri())
        })
    }

    private fun stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        viewModel.onMediaStateChanged(MediaState.STOP)
    }

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@ParseActivity
            vm = viewModel
            executePendingBindings()
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun chooseMedia(type: ChooserType, title: String, requestCode: Int) {
        val chooserIntent =
            createPickerIntent(type, title)
        startActivityForResult(chooserIntent, requestCode)
    }

    override fun configCreationEvents() {
        initializeVisualizerEngineWithPermissionCheck()
    }

    private fun play(rec: Recording) = rec.parsedWavePath?.toUri()?.let { uri -> play(uri) }

    private fun play(uri: Uri) {
        stopPlaying()
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer?.setOnCompletionListener {
            viewModel.onMediaStateChanged(MediaState.STOP)
        }
        viewModel.onMediaStateChanged(MediaState.PLAY)
        mediaPlayer?.start()
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    fun initializeVisualizerEngine() {
        audioVisualization = visualizer_view
        val visualizerHandler = DbmHandler.Factory.newVisualizerHandler(this, 0)
        audioVisualization?.linkTo(visualizerHandler)
    }

    public override fun onResume() {
        super.onResume()
        audioVisualization?.onResume()
    }

    public override fun onPause() {
        audioVisualization?.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        audioVisualization?.release()
        super.onDestroy()
    }

    override fun configResumeEvents() = Unit

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE -> {
                if (resultCode == Activity.RESULT_CANCELED) return
                viewModel.selectImageFile(data)
            }
        }
    }
}