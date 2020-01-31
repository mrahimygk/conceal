package ir.mrahimy.conceal

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.set
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
import kotlin.math.absoluteValue


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
                File("/storage/emulated/0/Download/8k16bitpcm.wav")
            )
        )

        val audioDataAsRgbList = waveFile.data.mapToUniformDouble().mapToRgbValue()

        val image = BitmapFactory.decodeFile("/storage/emulated/0/Download/deer.jpg")

        val rgbList = image.getRgbArray().remove3Lsb()

        val audioSampleRate = waveFile.sampleRate.toString().toSeparatedDigits()

        val sampleRateElementCount = audioSampleRate.elementCount.toBinString(format = "%4s")

        var position = 0

        var binaryString2BitsChunk = sampleRateElementCount.substring(0, 2).toInt()
        rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
        position += 1

        binaryString2BitsChunk = sampleRateElementCount.substring(2, 4).toInt()
        rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
        position += 1

        repeat(audioSampleRate.elementCount) {
            val element = it.toBinString(format = "%4s")
            binaryString2BitsChunk = element.substring(0, 2).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            binaryString2BitsChunk = element.substring(2, 4).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1
        }

        var shouldChangeTheLayer = false
        var lastIndexOfWaveDataChecked = 0
        var dataExceedsTheContainer = false

        audioDataAsRgbList.forEachIndexed { index, item ->
            if (position + 3 > (image.width - 1) * (image.height - 1)) {
                shouldChangeTheLayer = true
                /** breaks this for each*/
                return
            }

            val data = if (item < 0) {
                val negativeHandled = rgbList[position].r.bitwiseOr(4)
                rgbList[position].r = negativeHandled
                item.absoluteValue
            } else item

            var element = data.toBinString()
            binaryString2BitsChunk = element.substring(0, 2).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            element = data.toBinString()
            binaryString2BitsChunk = element.substring(2, 4).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            element = data.toBinString()
            binaryString2BitsChunk = element.substring(4, 6).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            element = data.toBinString()
            binaryString2BitsChunk = element.substring(6, 8).toInt()
            rgbList[position].r = rgbList[position].r.bitwiseOr(binaryString2BitsChunk)
            position += 1

            lastIndexOfWaveDataChecked = index
        }

        if (shouldChangeTheLayer) {
            shouldChangeTheLayer = false
            position = 0

            audioDataAsRgbList.forEachIndexed { index, item ->
                if (index < lastIndexOfWaveDataChecked) {
                    /** continues this forEach to the next element */
                    return@forEachIndexed
                }

                if (position + 3 > (image.width - 1) * (image.height - 1)) {
                    shouldChangeTheLayer = true
                    /** breaks this for each*/
                    return
                }

                val data = if (item < 0) {
                    val negativeHandled = rgbList[position].g.bitwiseOr(4)
                    rgbList[position].g = negativeHandled
                    item.absoluteValue
                } else item

                var element = data.toBinString()
                binaryString2BitsChunk = element.substring(0, 2).toInt()
                rgbList[position].g = rgbList[position].g.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(2, 4).toInt()
                rgbList[position].g = rgbList[position].g.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(4, 6).toInt()
                rgbList[position].g = rgbList[position].g.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(6, 8).toInt()
                rgbList[position].g = rgbList[position].g.bitwiseOr(binaryString2BitsChunk)
                position += 1

                lastIndexOfWaveDataChecked = index
            }
        }

        if (shouldChangeTheLayer) {
            shouldChangeTheLayer = false
            position = 0

            audioDataAsRgbList.forEachIndexed { index, item ->
                if (index < lastIndexOfWaveDataChecked) {
                    /** continues this forEach to the next element */
                    return@forEachIndexed
                }

                if (position + 3 > (image.width - 1) * (image.height - 1)) {
                    dataExceedsTheContainer = true
                    /** breaks this for each*/
                    return
                }

                val data = if (item < 0) {
                    val negativeHandled = rgbList[position].b.bitwiseOr(4)
                    rgbList[position].b = negativeHandled
                    item.absoluteValue
                } else item

                var element = data.toBinString()
                binaryString2BitsChunk = element.substring(0, 2).toInt()
                rgbList[position].b = rgbList[position].b.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(2, 4).toInt()
                rgbList[position].b = rgbList[position].b.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(4, 6).toInt()
                rgbList[position].b = rgbList[position].b.bitwiseOr(binaryString2BitsChunk)
                position += 1

                element = data.toBinString()
                binaryString2BitsChunk = element.substring(6, 8).toInt()
                rgbList[position].b = rgbList[position].b.bitwiseOr(binaryString2BitsChunk)
                position += 1

                lastIndexOfWaveDataChecked = index
            }
        }

        val bitmap = Bitmap.createBitmap(image.width, image.height, image.config)

        var x = 0
        var y = 0
        repeat(rgbList.size) { l ->
            val rgb = rgbList[l]
            bitmap[x, y] = Color.rgb(rgb.r,rgb.g,rgb.b)
            x++
            if (x >= image.width) {
                y++
                x = 0
            }
        }

        img?.setImageBitmap(bitmap)

        val date = Date()
        File(externalCacheDir?.absolutePath + "/img_${date.time}.png")
            .writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 100)

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
