package ir.mrahimy.conceal.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.github.squti.androidwaverecorder.WaveRecorder
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseActivity
import ir.mrahimy.conceal.databinding.ActivityMainBinding
import ir.mrahimy.conceal.util.EventObsrver
import ir.mrahimy.conceal.util.WavUtil.fromWaveData
import ir.mrahimy.conceal.util.Wave
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.util.*


@RuntimePermissions
class MainActivity : BaseActivity<MainActivityViewModel, ActivityMainBinding>() {

    var isRecording = false
    val PICK_IMAGE = 1
    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String

    override val layoutRes = R.layout.activity_main
    override val viewModel: MainActivityViewModel by viewModel()

    override fun bindObservables() {
        viewModel.onStartRecording.observe(this, EventObsrver {
            startRecordingWithPermissionCheck()
        })

        viewModel.onChooseImage.observe(this, EventObsrver {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, PICK_IMAGE)
        })
    }

    override fun initBinding() {
        binding.apply {
            lifecycleOwner = this@MainActivity
            vm = viewModel
            executePendingBindings()
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun openWave() {

    }

    override fun configCreationEvents() {
        openWaveWithPermissionCheck()
    }

    override fun configResumeEvents() = Unit

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    fun startRecording() {
        val date = Date()

        if (isRecording) {
            waveRecorder.stopRecording()
            isRecording = false
            val waveFile = fromWaveData(Wave.WavFile.openWavFile(File(filePath)))
            return
        }

        filePath = externalCacheDir?.absolutePath + "/rec_${date.time}.wav"
        waveRecorder = WaveRecorder(filePath)
        waveRecorder.startRecording()
        isRecording = true
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
                Log.d("onActivityResult", data?.data.toString())
            }
        }
    }
}
