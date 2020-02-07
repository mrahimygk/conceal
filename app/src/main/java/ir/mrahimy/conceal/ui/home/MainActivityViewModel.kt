package ir.mrahimy.conceal.ui.home

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseAndroidViewModel
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.data.capsules.SaveBitmapInfoCapsule
import ir.mrahimy.conceal.data.mapToRgbValue
import ir.mrahimy.conceal.data.mapToUniformDouble
import ir.mrahimy.conceal.util.*
import ir.mrahimy.conceal.util.ktx.getPath
import ir.mrahimy.conceal.util.ktx.getRgbArray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainActivityViewModel(
    application: Application,
    model: MainActivityModel
) : BaseAndroidViewModel(application, model) {

    private val _recordings = MutableLiveData<List<Recording>>()
    val recordings: LiveData<List<Recording>>
        get() = _recordings

    private val _onStartRecording = MutableLiveData<StatelessEvent>()
    val onStartRecording: LiveData<StatelessEvent>
        get() = _onStartRecording

    private val _inputImage = MutableLiveData<Bitmap>(null)
    val inputImage: LiveData<Bitmap>
        get() = _inputImage

    val isInputHintVisible = _inputImage.map { it == null }

    private val _inputWave = MutableLiveData<File>()
    val inputWave: LiveData<File>
        get() = _inputWave

    private val _percent = MutableLiveData<String>()
    val percent: LiveData<String>
        get() = _percent

    private val _showRecordTooltip = MutableLiveData<Boolean>(false)
    val showRecordTooltip: LiveData<Boolean>
        get() = _showRecordTooltip

    private val _outputImageLabel = MutableLiveData<Int>(R.string.choose_input_image)
    val outputImageLabel: LiveData<Int>
        get() = _outputImageLabel

    private val _waveFileLabel =
        MutableLiveData<String>(getApplication().getString(R.string.click_to_open_file))
    val waveFileLabel: LiveData<String>
        get() = _waveFileLabel

    /**
     * on active recording:  android.R.drawable.presence_audio_online
     * on inactive recording:  android.R.drawable.presence_audio_busy
     */
    private val _recordingIcon = MutableLiveData<Int>(android.R.drawable.presence_audio_busy)
    val recordingIcon: LiveData<Int>
        get() = _recordingIcon

    val isOutputHintVisible =
        combine(_percent, _inputImage, _inputWave) { percent, inputImage, inputWave ->
            if (inputImage == null) {
                _outputImageLabel.postValue(R.string.choose_input_image)
                return@combine true
            }

            if (inputWave == null) {
                _outputImageLabel.postValue(R.string.choose_wave_file_label)
                return@combine true
            }

            return@combine false
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
        viewModelScope.launch {
            delay(2000)
            _showRecordTooltip.postValue(true)
        }

//        viewModelScope.launch {
//            repeat(10) {
//                delay(90)
//                addRecording(it)
//            }
//        }
    }

    private fun addRecording(it: Int) = viewModelScope.launch {
        val recList = _recordings.value?.toMutableList() ?: mutableListOf()
        recList.add(
            Recording(
                it,
                "/storage/",
                null,
                Date()
            )
        )
        _recordings.postValue(recList)
    }

    fun startRecording() {
        _showRecordTooltip.postValue(false)
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

    fun chooseImage() {
        _onChooseImage.postValue(StatelessEvent())
    }

    private val _onChooseAudio = MutableLiveData<StatelessEvent>()
    val onChooseAudio: LiveData<StatelessEvent>
        get() = _onChooseAudio

    fun chooseAudio() {
        _onChooseAudio.postValue(StatelessEvent())
    }

    fun selectImageFile(data: Intent?) {
        viewModelScope.launch {
            //TODO: set loading input image to true
            delay(20)
            data?.data?.let {
                delay(20)
                val file = it.getPath(getApplication().applicationContext)
                delay(20)
                _inputImage.postValue(BitmapFactory.decodeFile(file))
            }
        }
    }

    fun selectAudioFile(data: Intent?) {
        viewModelScope.launch {
            //TODO: set loading wave to true
            delay(20)
            data?.data?.getPath(getApplication().applicationContext)?.let {
                _waveFileLabel.postValue(it)
                delay(20)
                _inputWave.postValue(File(it))
            }
        }
    }
}
