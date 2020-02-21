package ir.mrahimy.conceal.ui.parse

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import ir.mrahimy.conceal.R
import ir.mrahimy.conceal.base.BaseAndroidViewModel
import ir.mrahimy.conceal.data.MediaState
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.capsules.SaveWaveInfoCapsule
import ir.mrahimy.conceal.data.capsules.save
import ir.mrahimy.conceal.enums.FileSavingState
import ir.mrahimy.conceal.enums.RevealState
import ir.mrahimy.conceal.util.Event
import ir.mrahimy.conceal.util.HugeFileException
import ir.mrahimy.conceal.util.StatelessEvent
import ir.mrahimy.conceal.util.combine
import ir.mrahimy.conceal.util.ktx.*
import kotlinx.coroutines.*
import java.util.*

class ParseActivityViewModel(
    application: Application,
    private val model: ParseActivityModel
) : BaseAndroidViewModel(application, model) {

    private val waveFileSavingState = MutableLiveData<FileSavingState>(FileSavingState.IDLE)
    private val revealState = MutableLiveData<RevealState>(RevealState.IDLE)
    private val _mediaState = MutableLiveData<MediaState>(MediaState.STOP)

    val isDoneMarkVisible =
        combine(waveFileSavingState, revealState) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.DONE && revealState == RevealState.DONE
        }

    val isRevealing =
        combine(waveFileSavingState, revealState) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.IDLE && revealState == RevealState.REVEALING
        }

    val isProcessing =
        combine(waveFileSavingState, revealState) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.SAVING || revealState == RevealState.REVEALING
        }

    val drawable: LiveData<Int> =
        combine(waveFileSavingState, _mediaState) { waveFileSavingState, mediaState ->
            when {
                mediaState == MediaState.PLAY -> R.drawable.ic_stop_fill
                waveFileSavingState == FileSavingState.DONE -> R.drawable.ic_play_fill
                else -> R.drawable.wav
            }
        }

    private val inputImagePath = MutableLiveData<String>()
    private val _inputImage = MutableLiveData<Bitmap>(null)
    val inputImage: LiveData<Bitmap>
        get() = _inputImage

    val isInputHintVisible = _inputImage.map { it == null }

    private val _inputImageSelectionTooltip = MutableLiveData<Int>(null)
    val inputImageSelectionTooltip: LiveData<Int>
        get() = _inputImageSelectionTooltip

    /**
     * changes on revealing job done to the path of the revealed audio file
     */
    private val _waveFileLabel = MutableLiveData<String>(getString(R.string.output_wave_path))
    val waveFileLabel: LiveData<String>
        get() = _waveFileLabel

    private val _snackMessage = MutableLiveData<Event<Int>>()
    val snackMessage: LiveData<Event<Int>>
        get() = _snackMessage

    /**
     * errors:
     *      no sample rate or invalid sample rate
     *      none or invalid other essential info at first
     */
    private val _inputError = MutableLiveData<String>(getString(R.string.empty))
    val outputHintTextColor = _inputError.map {
        /**
         * could not find the best color for error
         */
        if (it.isNullOrBlank()) getColor(R.color.text_color)
        else getColor(R.color.text_color)
    }

    val handle = _inputImage.map {
        if (it == null) return@map 1
        parseWaveFileFromImage(it)
        1
    }

    private val _isInputImageLoading = MutableLiveData<Boolean>(false)
    val isInputImageLoading: LiveData<Boolean>
        get() = _isInputImageLoading

    val isSavingFileTextVisible = waveFileSavingState.map {
        it == FileSavingState.SAVING
    }

    private lateinit var revealJob: Job
    private lateinit var saveFileJob: Job

    fun cancelRevealJob() {
        if (::revealJob.isInitialized)
            revealJob.cancel()
        if (::saveFileJob.isInitialized)
            saveFileJob.cancel()

        viewModelScope.launch {
            delay(10)
            waveFileSavingState.postValue(FileSavingState.IDLE)
            revealState.postValue(RevealState.IDLE)
        }
    }

    private var recordingToInsert: Recording? = null

    private fun parseWaveFileFromImage(image: Bitmap) {
        revealJob = Job()
        viewModelScope.launch(revealJob + Dispatchers.Default) {
            revealState.postValue(RevealState.REVEALING)
            delay(10)
            val waver = withContext(revealJob + Dispatchers.IO) {
                val waver = image.parseWaver()
                waver
            }
            revealState.postValue(RevealState.DONE)

            saveFileJob = Job()
            waveFileSavingState.postValue(FileSavingState.SAVING)
            getApplication().applicationContext.externalCacheDir?.absolutePath?.let {

                val carrierImagePath = inputImagePath.value ?: return@launch
                val imageName = carrierImagePath.getNameFromPath()
                val wavInfo = SaveWaveInfoCapsule("${imageName}_parsed", Date(), waver)
                val parsedWavePath = withContext(saveFileJob + Dispatchers.IO) {
                    try {
                        wavInfo.save(it)
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        e.printStackTrace()
                        tellDataExceeds(e)
                        null
                    }
                }

                parsedWavePath?.let { wavePath ->
                    _waveFileLabel.postValue(wavePath.removeEmulatedPath())
                    recordingToInsert = Recording(
                        0L,
                        null,
                        carrierImagePath,
                        wavePath,
                        wavePath,
                        Date().time
                    )
                }
            }
            waveFileSavingState.postValue(FileSavingState.DONE)
        }
    }

    private fun tellDataExceeds(e: Exception) {
        val stringRes =
            if (e is HugeFileException) getString(R.string.data_cannot_be_parsed_on_index, e.index)
            else getString(R.string.data_size_does_not_match)

        _inputError.postValue(stringRes)
        viewModelScope.launch {
            delay(10)
            cancelRevealJob()
        }
    }

    private val _onStopPlaying = MutableLiveData<StatelessEvent>()
    val onStopPlaying: LiveData<StatelessEvent>
        get() = _onStopPlaying

    private val _onChooseImage = MutableLiveData<StatelessEvent>()
    val onChooseImage: LiveData<StatelessEvent>
        get() = _onChooseImage

    fun chooseImage() {
        if (isProcessing.value == true) {
            _snackMessage.postValue(Event(R.string.please_cancel_first))
            return
        }

        _onChooseImage.postValue(StatelessEvent())
    }

    fun selectImageFile(data: Intent?) {
        data?.data?.let {
            selectImageFile(it.getPathJava(getApplication().applicationContext))
        }
    }

    private fun selectImageFile(file: String) {
        viewModelScope.launch {
            _isInputImageLoading.postValue(true)
            delay(10)
            inputImagePath.postValue(file)
            _inputImage.postValue(file.loadBitmap())
            _isInputImageLoading.postValue(false)
        }
    }

    fun onMediaStateChanged(mediaState: MediaState) {
        _mediaState.postValue(mediaState)
    }

    private val _onPlayOutputAudio = MutableLiveData<Event<String>>()
    val onPlayOutputAudio: LiveData<Event<String>>
        get() = _onPlayOutputAudio

    fun playAudio() {
        val revealState = revealState.value ?: return
        val savingState = waveFileSavingState.value ?: return
        val mediaState = _mediaState.value ?: return

        if (revealState == RevealState.DONE && savingState == FileSavingState.DONE) {
            if (mediaState == MediaState.PLAY)
                _onPlayOutputAudio.postValue(Event("stop"))
            else {
                val path = recordingToInsert?.parsedWavePath ?: return
                _onPlayOutputAudio.postValue(Event(path))
            }
        }
    }

    private val _onDoneInserting = MutableLiveData<StatelessEvent>()
    val onDoneInserting: LiveData<StatelessEvent>
        get() = _onDoneInserting

    fun insert() = viewModelScope.launch {
        if (recordingToInsert == null) {
            _snackMessage.postValue(Event(R.string.no_input_set))
            _inputImageSelectionTooltip.postValue(R.string.select_image_tooltip)
            return@launch
        } else recordingToInsert?.let {
            model.addRecording(it)
            _onDoneInserting.postValue(StatelessEvent())
        }
    }
}