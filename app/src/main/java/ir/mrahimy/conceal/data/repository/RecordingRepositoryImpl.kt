package ir.mrahimy.conceal.data.repository

import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.db.dao.RecordingDao

class RecordingRepositoryImpl(private val recordingDao: RecordingDao) : RecordingRepository {
    override suspend fun addRecording(recording: Recording) {
        recordingDao.upsertRecording(recording)
    }

    override suspend fun deleteRecording(recording: Recording) {
        recordingDao.deleteRecording(recording)
    }

    override fun getAllRecordings() = recordingDao.getRecordings()
}