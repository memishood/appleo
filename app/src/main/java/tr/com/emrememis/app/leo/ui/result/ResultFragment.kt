package tr.com.emrememis.app.leo.ui.result

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.util.FileUtils
import tr.com.emrememis.app.leo.util.Utils
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext

class ResultFragment : Fragment(), CoroutineScope {

    companion object { const val github = "https://github.com/memishood" }

    private val job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatImageView5.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(github)))
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {

            val video = File(it.getString("path") ?: return@let )
            val music = it.getInt("music")
            val clip = it.getInt("clip", -1)

            launch {

                val audio = FileUtils.musicToFile(context, resources, music)
                val duration = FileUtils.getFileDuration(video.absolutePath)
                val outputPath = "${video.absolutePath}${UUID.randomUUID()}.mp4"

                val cmd = when (clip) {
                    -1 ->  Utils.cmd(video.absolutePath, audio.absolutePath, duration, outputPath)
                    else -> Utils.cmd(video.absolutePath, audio.absolutePath, FileUtils.clipToFile(context, clip).absolutePath, duration, outputPath)
                }

                val result = FFmpeg.execute(cmd)

                audio.delete()

                val resolver = context?.contentResolver ?: return@launch

                val uri = when {
                    Build.VERSION.SDK_INT >= 29 -> FileUtils.saveOutputWithPending(resolver, File(outputPath))
                    else -> FileUtils.saveOutput(resolver, File(outputPath))
                }

                uri ?: return@launch

                setUi(uri = uri)

            }

        }
    }

    //@MainThread
    private suspend fun setUi(uri: Uri) = withContext(Dispatchers.Main) {
        videoView.setVideoURI(uri)
        videoView.start()
        videoView.setOnCompletionListener { videoView.start() }

        val parent = progressBar2.parent as ViewGroup
        parent.removeView(progressBar2)
    }

    override fun onDestroy() {
        FFmpeg.cancel()
        job.cancel()
        super.onDestroy()
    }
}