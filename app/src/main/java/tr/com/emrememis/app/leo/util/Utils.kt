package tr.com.emrememis.app.leo.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.core.graphics.drawable.toBitmap
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.models.Clip
import tr.com.emrememis.app.leo.data.models.Music
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Utils {

    val musics = listOf(
        Music("Neffex", "Best of Me", R.drawable.neffex, R.raw.bestofme),
        Music("Neffex", "Without You", R.drawable.neffex, R.raw.withoutyou),
        Music("Neffex", "One Shot", R.drawable.neffex, R.raw.oneshot)
    )

    val clips = listOf(
        Clip(R.drawable.ic_facebook),
        Clip(R.drawable.ic_groupme),
        Clip(R.drawable.ic_instagram),
        Clip(R.drawable.ic_linkedin),
        Clip(R.drawable.ic_periscope),
        Clip(R.drawable.ic_reddit),
        Clip(R.drawable.ic_skype),
        Clip(R.drawable.ic_snapchat),
        Clip(R.drawable.ic_tik_tok),
        Clip(R.drawable.ic_tumblr),
        Clip(R.drawable.ic_twitter),
        Clip(R.drawable.ic_whatsapp),
        Clip(R.drawable.ic_youtube)
    )

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

    fun cmd(videoPath: String, audioPath: String, imagePath: String, duration: String, outputPath: String): Array<String> = arrayOf(
        "-i", videoPath,
        "-i", imagePath,
        "-i", audioPath,

        "-t", duration,

        "-filter_complex",  "[0:v][1:v] overlay=(W-w)/2:(H-h)/2:enable='between(t,0,20)'",

        "-c:a", "copy",
        "-c:a", "aac",

        "-map", "0:v:0",
        "-map", "2:a:0",

        "-shortest",

        outputPath
    )

    fun cmd(videoPath: String, audioPath: String, duration: String, outputPath: String): Array<String> = arrayOf(
        "-i", videoPath,
        "-i", audioPath,

        "-t", duration,

        "-c:v", "copy",
        "-c:a", "aac",

        "-map", "0:v:0",
        "-map", "1:a:0",

        "-shortest",
        outputPath
    )
}