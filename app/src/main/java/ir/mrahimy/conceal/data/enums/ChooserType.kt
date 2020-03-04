package ir.mrahimy.conceal.data.enums

import android.net.Uri
import android.provider.MediaStore

enum class ChooserType(val typeString: String, val externalContentUri: Uri) {
    Image("image/*", MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
    Audio("audio/x-wav", MediaStore.Audio.Media.EXTERNAL_CONTENT_URI),
}