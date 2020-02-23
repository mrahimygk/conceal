package ir.mrahimy.conceal.ui.home

import ir.mrahimy.conceal.base.BaseModel
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.Waver
import ir.mrahimy.conceal.net.req.makeAudioInfoMap
import ir.mrahimy.conceal.repository.InfoRepository
import ir.mrahimy.conceal.repository.RecordingRepository
import java.io.File

class MainActivityModel(
    private val recordingRepository: RecordingRepository,
    private val infoRepository: InfoRepository
) : BaseModel() {

    suspend fun addRecording(recording: Recording) = recordingRepository.addRecording(recording)

    suspend fun deleteRecording(recording: Recording) =
        recordingRepository.deleteRecording(recording)

    fun getAllRecordings() = recordingRepository.getAllRecordings()

//    suspend fun putInputImageData() = infoRepository.putImageInfo(null)

    suspend fun putInputWaveData(waver: Waver, file: File, isParsed: Boolean) =
        infoRepository.putAudioInfo(waver.makeAudioInfoMap(isParsed, file))
}