package ir.mrahimy.conceal.ui.parse

import ir.mrahimy.conceal.base.BaseModel
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.repository.RecordingRepository

class ParseActivityModel(private val repository: RecordingRepository) : BaseModel() {
    suspend fun addRecording(recording: Recording) = repository.addRecording(recording)
}