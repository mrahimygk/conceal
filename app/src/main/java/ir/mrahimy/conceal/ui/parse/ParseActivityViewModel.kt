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
import ir.mrahimy.conceal.data.enums.FileSavingState
import ir.mrahimy.conceal.data.enums.RevealState
import ir.mrahimy.conceal.net.req.makeAudioInfoMap
import ir.mrahimy.conceal.net.req.makeImageInfoMap
import ir.mrahimy.conceal.repository.InfoRepository
import ir.mrahimy.conceal.repository.RecordingRepository
import ir.mrahimy.conceal.util.HugeFileException
import ir.mrahimy.conceal.util.arch.Event
import ir.mrahimy.conceal.util.arch.StatelessEvent
import ir.mrahimy.conceal.util.arch.combine
import ir.mrahimy.conceal.util.ktx.getNameFromPath
import ir.mrahimy.conceal.util.ktx.getPathJava
import ir.mrahimy.conceal.util.ktx.loadBitmap
import ir.mrahimy.conceal.util.ktx.parseWaver
import ir.mrahimy.conceal.util.ktx.removeEmulatedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date

private const val BACK_PRESS_EXIT_TIME = 2000L

class ParseActivityViewModel(
    application: Application,
    private val recordingRepository: RecordingRepository,
    private val infoRepository: InfoRepository
) : BaseAndroidViewModel(application) {

    private val waveFileSavingState = MutableLiveData<FileSavingState>(FileSavingState.IDLE)
    private val revealState = MutableLiveData<RevealState>(RevealState.IDLE)
    private val _mediaState = MutableLiveData<MediaState>(MediaState.STOP)

    val isDoneMarkVisible =
        combine(
            waveFileSavingState,
            revealState
        ) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.DONE && revealState == RevealState.DONE
        }

    val isRevealing =
        combine(
            waveFileSavingState,
            revealState
        ) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.IDLE && revealState == RevealState.REVEALING
        }

    val isProcessing =
        combine(
            waveFileSavingState,
            revealState
        ) { waveFileSavingState, revealState ->
            waveFileSavingState == FileSavingState.SAVING || revealState == RevealState.REVEALING
        }

    val drawable: LiveData<Int> =
        combine(
            waveFileSavingState,
            _mediaState
        ) { waveFileSavingState, mediaState ->
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
    val handle = _inputImage.map {
        if (it == null) return@map 1
        viewModelScope.launch {
            val path = inputImagePath.value ?: return@launch
            val file = File(path)
            /**
             * this is not the parsed wave, this is the actual selected file
             * TODO: get the result -> put again
             */
            infoRepository.putImageInfo(it.makeImageInfoMap(true, file))
        }
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
                try {
                    image.parseWaver()
                } catch (e: NumberFormatException) {
                    cancelRevealJob()
                    _snackMessage.postValue(Event(R.string.error_in_parsing_image))
                    return@withContext null
                }
            }

            revealState.postValue(RevealState.DONE)
            saveFileJob = Job()
            waveFileSavingState.postValue(FileSavingState.SAVING)
            getApplication().applicationContext.externalCacheDir?.absolutePath?.let {
                val carrierImagePath = inputImagePath.value ?: return@launch
                val imageName = carrierImagePath.getNameFromPath()
                val wavInfo = waver?.let {
                    SaveWaveInfoCapsule("${imageName}_parsed", Date(), waver)
                }
                val parsedWavePath = withContext(saveFileJob + Dispatchers.IO) {
                    try {
                        wavInfo?.save(it)
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        e.printStackTrace()
                        tellDataExceeds(e)
                        null
                    }
                }

                parsedWavePath?.let { wavePath ->
                    viewModelScope.launch api@{
                        val file = File(wavePath)
                        /**
                         * this is not the parsed wave, this is the actual selected file
                         * TODO: get the result -> put again
                         */
                        waver?.let {
                            infoRepository.putAudioInfo(waver.makeAudioInfoMap(true, file))
                        }
                    }
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
        if (isProcessing.value == true) {
            _snackMessage.postValue(Event(R.string.please_cancel_first))
            return@launch
        }

        if (recordingToInsert == null) {
            _snackMessage.postValue(Event(R.string.no_input_set))
            _inputImageSelectionTooltip.postValue(R.string.select_image_tooltip)
            return@launch
        }

        recordingToInsert?.let {
            recordingRepository.addRecording(it)
            _onDoneInserting.postValue(StatelessEvent())
        }
    }

    private val mustExit = MutableLiveData<Boolean>().apply { value = false }

    fun onBackPressed() {
        if (isProcessing.value == true) {
            _snackMessage.postValue(Event(R.string.please_cancel_first))
            return
        }

        if (recordingToInsert == null) {
            _onDoneInserting.postValue(StatelessEvent())
            return
        }

        if (mustExit.value == true) {
            _onDoneInserting.postValue(StatelessEvent())
            return
        }

        mustExit.postValue(true)
        _snackMessage.postValue(Event(R.string.please_insert_first))
        viewModelScope.launch {
            delay(BACK_PRESS_EXIT_TIME)
            mustExit.postValue(false)
        }
    }
}