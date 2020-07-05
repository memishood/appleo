package tr.com.emrememis.app.leo.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

object UriToPath {

    fun getFilePath(context: Context?, uri: Uri): String? {

        var data = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        when {
            isExternalStorageDocument(data) -> {
                val docId = DocumentsContract.getDocumentId(data)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            isDownloadsDocument(data) -> {
                val id = DocumentsContract.getDocumentId(data)
                data = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
            }

            isMediaDocument(data) -> {
                val docId = DocumentsContract.getDocumentId(data)
                val split = docId.split(":").toTypedArray()
                val type = split[0]

                when (type) {

                    "image" -> {
                        data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }

                    "video" -> {
                        data = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }

                    "audio" -> {
                        data = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                }

                selection = "_id=?"
                selectionArgs = arrayOf(split[1])

            }

        }

        if ("content".equals(data.scheme, ignoreCase = true)) {

            if (isGooglePhotosUri(data)) {
                return data.lastPathSegment
            }

            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context?.contentResolver?.query(data, projection, selection, selectionArgs, null) ?: return null
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            cursor.moveToFirst()

            val path = cursor.getString(index)

            cursor.close()

            return path

        } else if ("file".equals(data.scheme, ignoreCase = true)) {
            return data.path
        }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}