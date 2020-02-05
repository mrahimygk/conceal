package ir.mrahimy.conceal

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.squti.androidwaverecorder.WaveRecorder
import ir.mrahimy.conceal.data.WavUtil.fromWaveData
import ir.mrahimy.conceal.data.mapToRgbValue
import ir.mrahimy.conceal.data.mapToUniformDouble
import ir.mrahimy.conceal.util.*
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

        val waveFile = fromWaveData(
            Wave.WavFile.openWavFile(File("/storage/emulated/0/Download/8k16bitpcm.wav"))
        )

        val audioDataAsRgbList = waveFile.data.mapToUniformDouble().mapToRgbValue()

        val image = BitmapFactory.decodeFile("/storage/emulated/0/Download/deer.jpg")

        val rgbList = image.getRgbArray().remove3Lsb()

        val position = rgbList.putSampleRate(waveFile.sampleRate.toInt())

        rgbList.putAllSignedIntegers(position, audioDataAsRgbList, image.width, image.height)

        val bitmap = rgbList.toBitmap(image)

        img?.setImageBitmap(bitmap)

        val date = Date()
        File(externalCacheDir?.absolutePath + "/img_${date.time}.png")
            .writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 100)

        record?.setOnClickListener {
            startRecording()
        }
    }

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
}
