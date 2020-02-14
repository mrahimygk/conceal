package ir.mrahimy.conceal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.mrahimy.conceal.data.Recording
import ir.mrahimy.conceal.data.db.dao.RecordingDao

@Database(
    entities = [Recording::class],
    version = 1, exportSchema = false
)
abstract class ConcealDb : RoomDatabase() {

    abstract fun recordingDao(): RecordingDao
}