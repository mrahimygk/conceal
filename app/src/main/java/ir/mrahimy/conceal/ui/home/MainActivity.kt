package ir.mrahimy.conceal.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.google.android.material.snackbar.Snackbar
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseActivity
import ir.mrahimy.conceal.databinding.ActivityMainBinding
import ir.mrahimy.conceal.enums.ChooserType
import ir.mrahimy.conceal.util.EventObsrver
import ir.mrahimy.conceal.util.putAllSignedIntegers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import androidx.core.net.toUri
import ir.mrahimy.conceal.data.MediaState
import ir.mrahimy.conceal.data.Recording

const val PICK_IMAGE = 1000
const val PICK_AUDIO = 2000

@RuntimePermissions
class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    override val layoutRes = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()

    private val adapter: RecordingsAdapter by inject()

    private var audioVisualization: AudioVisualization? = null

    private var mediaPlayer: MediaPlayer? = null

    override fun bindObservables() {
        viewModel.onStartRecording.observe(this, EventObsrver {
            startRecordingWithPermissionCheck()
        })

        viewModel.onChooseImage.observe(this, EventObsrver {
            chooseMediaWithPermissionCheck(
                ChooserType.Image,
                getString(R.string.select_image_title),
                PICK_IMAGE
            )
        })

        viewModel.onChooseAudio.observe(this, EventObsrver {
            chooseMediaWithPermissionCheck(
                ChooserType.Audio,
                getString(R.string.select_audio_title),
                PICK_AUDIO
            )
        })

        viewModel.onStartRgbListPutAll.observe(this, EventObsrver { input ->
            input.apply {
                rgbList.putAllSignedIntegers(position, audioDataAsRgbList, refImage, job)
                    .observe(this@MainActivity, Observer {
                        viewModel.onUpdateInserting(it)
                    })
            }
        })

        viewModel.snackMessage.observe(this, EventObsrver {
            Snackbar.make(recordings_list, it, Snackbar.LENGTH_LONG).show()
        })

        viewModel.onAddingMaxAmplitude.observe(this, EventObsrver {
            recording_visualizer_view?.addAmplitude(it)
        })

        viewModel.onDataExceeds.observe(this, EventObsrver {
            Snackbar.make(recordings_list, R.string.data_exceeds, Snackbar.LENGTH_LONG).show()
        })

        viewModel.onStopPlaying.observe(this, EventObsrver {
            stopPlaying()
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
            lifecycleOwner = this@MainActivity
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
        recordings_list?.adapter = adapter

        adapter.onDelete = { rec: Recording, _: View ->
            viewModel.delete(rec)
        }

        adapter.onStop = { _: Recording, _: View ->
            stopPlaying()
        }

        adapter.onPlay = { recording: Recording, _: View ->
            play(recording)
        }

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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    fun startRecording() {
        viewModel.startRecordingWave()
    }

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

            PICK_AUDIO -> {
                if (resultCode == Activity.RESULT_CANCELED) return
                viewModel.selectAudioFile(data)
            }
        }
    }
}