package ir.mrahimy.conceal.data.repository

import androidx.lifecycle.LiveData
import ir.mrahimy.conceal.data.Recording

interface RecordingRepository {

    suspend fun addRecording(recording: Recording)
    suspend fun deleteRecording(recording: Recording)
    fun getAllRecordings(): LiveData<List<Recording>>
}