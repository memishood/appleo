package tr.com.emrememis.app.leo.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object FileUtils {

    fun musicToFile(context: Context?, resources: Resources, music: Int): File {
        val stream = resources.openRawResource(music)

        val file = File(context?.filesDir?.path + "/" + UUID.randomUUID().toString() + ".mp3")
        file.createNewFile()

        val fos = FileOutputStream(file)
        fos.write(stream.readBytes())
        fos.close()

        return file
    }

    fun getFileDuration(absolutePath: String): String {

        var retriever: MediaMetadataRetriever? = null
        var inputStream: FileInputStream? = null

        try {

            retriever = MediaMetadataRetriever()
            inputStream = FileInputStream(absolutePath)
            retriever.setDataSource(inputStream.fd)

            return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        }finally {

            retriever?.release()
            inputStream?.close()

        }

    }

    fun saveOutput(resolver: ContentResolver, outputFile: File): Uri? {

        if (!outputFile.exists()) {
            return null
        }

        val output = ContentValues()
        output.put(MediaStore.Video.Media.DISPLAY_NAME, "Leo_" + UUID.randomUUID().toString() + ".mp4")

        val outputUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, output) ?: return null

        resolver.openFileDescriptor(outputUri, "w")?.use {
            val fos = FileOutputStream(it.fileDescriptor)
            fos.write(outputFile.readBytes())
            fos.close()
        }

        outputFile.delete()

        return outputUri
    }

    @RequiresApi(29)
    fun saveOutputWithPending(resolver: ContentResolver, outputFile: File): Uri? {
        if (!outputFile.exists()) {
            return null
        }

        val output = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, "Leo_" + UUID.randomUUID().toString() + ".mp4")
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        val outputUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, output) ?: return null

        resolver.openFileDescriptor(outputUri, "w")?.use {
            val fos = FileOutputStream(it.fileDescriptor)
            fos.write(outputFile.readBytes())
            fos.close()
        }

        output.clear()
        output.put(MediaStore.Video.Media.IS_PENDING, 0)
        resolver.update(outputUri, output, null, null)

        outputFile.delete()

        return outputUri
    }


    fun clipToFile(context: Context?, clip: Int): File {

        val drawable = context?.getDrawable(clip)

        val bitmap = drawable?.toBitmap()
        val bot = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bot)
        val byteArray = bot.toByteArray()

        val file = File(context?.filesDir?.path + "/" + UUID.randomUUID().toString() + ".png")
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.close()

        return file
    }

}