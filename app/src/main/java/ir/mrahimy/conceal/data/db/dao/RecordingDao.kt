package ir.mrahimy.conceal.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mrahimy.conceal.data.Recording

@Dao
interface RecordingDao {
    @Query("SELECT * FROM recording")
    fun getRecordings(): LiveData<List<Recording>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecording(item: Recording)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecordings(items: List<Recording>)

    @Delete
    suspend fun deleteRecording(item: Recording)
}