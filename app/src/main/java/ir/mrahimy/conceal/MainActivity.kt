package ir.mrahimy.conceal

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.squti.androidwaverecorder.WaveRecorder
import ir.mrahimy.conceal.data.WavUtil.fromWaveData
import ir.mrahimy.conceal.data.mapToRgbValue
import ir.mrahimy.conceal.data.mapToUniformDouble
import ir.mrahimy.conceal.util.Wave
import ir.mrahimy.conceal.util.getRgbArray
import ir.mrahimy.conceal.util.remove3Lsb
import ir.mrahimy.conceal.util.toSeparatedDigits
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.util.*


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    var isRecording = false
    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askPermissions()
        val waveFile = fromWaveData(
            Wave.WavFile.openWavFile(
                File(
                    "/storage/emulated/0/Download/8k16bitpcm.wav"
                )
            )
        )

        val doubled = waveFile.data.mapToUniformDouble()
        val rgb = doubled.mapToRgbValue()

        val image = BitmapFactory.decodeFile(
            "/storage/emulated/0/Download/deer.jpg"
        )

        val _rgbList = image.getRgbArray()

        val rgbList = _rgbList.remove3Lsb()

        val rate = waveFile.sampleRate.toString().toSeparatedDigits()

        record?.setOnClickListener {
            startRecording()
        }
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun askPermissions() {

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
    private fun startRecording() {
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
}
