package ir.mrahimy.conceal.util

import android.graphics.Bitmap
import ir.mrahimy.conceal.data.Waver
import java.io.File

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int = 100) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

fun File.writeWave(waver: Waver) {
    waver.apply {
        Wave.WavFile.newWavFile(
            this@writeWave,
            channelCount,
            frameCount,
            validBits,
            sampleRate
        ).writeAllFrames(this)
    }
}

fun Wave.WavFile.writeAllFrames(waver: Waver) {
    WavUtil.writeAllFrames(this, waver)
}