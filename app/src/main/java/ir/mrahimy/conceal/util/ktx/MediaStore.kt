package ir.mrahimy.conceal.util.ktx

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import ir.mrahimy.conceal.util.ktx.FileUtils.getRealPath


fun Uri.getPath(context: Context): String? {
    when {
        DocumentsContract.isDocumentUri(context, this) -> // ExternalStorageProvider
            when {
                isExternalStorageDocument(this) -> {
                    val docId = DocumentsContract.getDocumentId(this)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return context.getExternalFilesDir(null)?.absolutePath + "/" + split[1]
                    }
                }
                isDownloadsDocument(this) -> {// DownloadsProvider
                    val id = DocumentsContract.getDocumentId(this)
                    if (id.startsWith("raw:"))
                        return id.replaceFirst("raw:", "")
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)

                }
                isMediaDocument(this) -> { // MediaProvider
                    val docId = DocumentsContract.getDocumentId(this)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)

                }
            }
        "content".equals(scheme, ignoreCase = true) -> // MediaStore (and general)
            // Return the remote address
            return if (isGooglePhotosUri(this)) lastPathSegment else getDataColumn(
                context,
                this,
                null,
                null
            )
        "file".equals(scheme, ignoreCase = true) -> // File
            return path
    }
    return null
}

fun getDataColumn(
    context: Context,
    inUri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    inUri?.let { uri ->
        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            cursor?.let { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            }
        } finally {
            cursor?.close()
        }
    }

    return null
}

fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.authority
}

fun Uri.getPathJava(context: Context): String = getRealPath(context, this)