package ir.mrahimy.conceal.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import ir.mrahimy.conceal.base.BaseAndroidViewModel
import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.data.capsules.SaveBitmapInfoCapsule
import ir.mrahimy.conceal.data.mapToRgbValue
import ir.mrahimy.conceal.data.mapToUniformDouble
import ir.mrahimy.conceal.util.*
import ir.mrahimy.conceal.util.ktx.getRgbArray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainActivityViewModel(
    application: Application,
    private val model: MainActivityModel
) : BaseAndroidViewModel(application, model) {

    private val _onStartRecording = MutableLiveData<StatelessEvent>()
    val onStartRecording: LiveData<StatelessEvent>
        get() = _onStartRecording

    private val _inputImage = MutableLiveData<Bitmap>()
    val inputImage: LiveData<Bitmap>
        get() = _inputImage

    val isInputHintVisible = _inputImage.map { it == null }

    private val _inputWave = MutableLiveData<File>()
    val inputWave: LiveData<File>
        get() = _inputWave

    private val _percent = MutableLiveData<String>()
    val percent: LiveData<String>
        get() = _percent

    /**
     * on active recording:  android.R.drawable.presence_audio_online
     * on inactive recording:  android.R.drawable.presence_audio_busy
     */
    private val _recordingIcon = MutableLiveData<Int>(android.R.drawable.presence_audio_busy)
    val recordingIcon: LiveData<Int>
        get() = _recordingIcon

    val isOutputHintVisible =
        combine(_percent, _inputImage, _inputWave) { percent, inputImage, inputWave ->
            return@combine inputImage == null
        }

    private val _waveInfo = _inputWave.map {
        if (it == null) return@map null
        WavUtil.fromWaveData(
            Wave.WavFile.openWavFile(it)
        )
    }

    val outputBitmap = combine(_inputImage, _waveInfo) { _image, _waveFile ->
        val image = _image ?: return@combine null
        val waveFile = _waveFile ?: return@combine null
        val outputBitmap = putWaveFileIntoImage(image, waveFile)

        getApplication().applicationContext.externalCacheDir?.absolutePath?.let {
            val info = SaveBitmapInfoCapsule("img", Date(), outputBitmap, Bitmap.CompressFormat.PNG)
            saveBitmap(it, info)
        }

        return@combine outputBitmap
    }

    private fun putWaveFileIntoImage(
        image: Bitmap,
        waveFile: Waver
    ): Bitmap {
        val rgbList = image.getRgbArray().remove3Lsb()
        val audioDataAsRgbList = waveFile.data.mapToUniformDouble().mapToRgbValue()
        val position = rgbList.putSampleRate(waveFile.sampleRate.toInt())

        rgbList.putAllSignedIntegers(position, audioDataAsRgbList, image.width, image.height)
        return rgbList.toBitmap(image)
    }

    init {

        _inputWave.postValue(File("/storage/emulated/0/Download/8k16bitpcm.wav"))

        viewModelScope.launch {
            delay(1000)
            _inputImage.postValue(
                BitmapFactory.decodeFile("/storage/emulated/0/Download/deer.jpg")
            )
        }

    }

    fun startRecording() {
        _onStartRecording.postValue(StatelessEvent())
    }

    private fun saveBitmap(path: String, fileInfo: SaveBitmapInfoCapsule) {
        File(
            path +
                    "${fileInfo.name}_${fileInfo.time?.time}." +
                    fileInfo.format.name.toLowerCase(Locale.ENGLISH)
        ).writeBitmap(fileInfo.bitmap, fileInfo.format, 100)
    }

    fun showSlide(position: Int) {
        val input = _inputImage.value ?: return
        val output = outputBitmap.value ?: return
        val bitmapArray = arrayOf(input, output)
        //TODO: navigate to slide show activity with bitmapArray & index position
    }

    private val _onChooseImage = MutableLiveData<StatelessEvent>()
    val onChooseImage: LiveData<StatelessEvent>
        get() = _onChooseImage

    fun chooseImage(){
        _onChooseImage.postValue(StatelessEvent())
    }
}
