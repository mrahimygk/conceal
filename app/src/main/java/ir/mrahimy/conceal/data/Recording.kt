package ir.mrahimy.conceal.data

import android.net.Uri
import java.util.*

data class Recording(
    val id: Int,
    val file: String,
    val uri: Uri,
    val date: Date
)