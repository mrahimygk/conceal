package ir.mrahimy.conceal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recording(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "inputImagePath")
    val inputImagePath: String,
    @ColumnInfo(name = "outputImagePath")
    val outputImagePath: String,
    @ColumnInfo(name = "inputWavePath")
    val inputWavePath: String,
    /**
     * After recording is done, we parse data on the run
     * But if they have received an image, there would be not parsedWavePath
     */
    @ColumnInfo(name = "parsedWavePath")
    val parsedWavePath: String?,
    @ColumnInfo(name = "date")
    val date: Long
)