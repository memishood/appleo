package tr.com.emrememis.app.leo.util

import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.models.Clip
import tr.com.emrememis.app.leo.data.models.Music

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