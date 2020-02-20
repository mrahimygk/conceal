package ir.mrahimy.conceal.ui.home

import ir.mrahimy.conceal.base.BaseModel
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.repository.RecordingRepository

class MainActivityModel(private val repository: RecordingRepository) : BaseModel() {

    suspend fun addRecording(recording: Recording) = repository.addRecording(recording)

    suspend fun deleteRecording(recording: Recording) = repository.deleteRecording(recording)

    fun getAllRecordings() = repository.getAllRecordings()
}