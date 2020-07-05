package tr.com.emrememis.app.leo.ui.result

import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.*
import tr.com.emrememis.app.leo.R
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext

class ResultFragment : Fragment(), CoroutineScope {

    companion object {
        const val github = "https://github.com/memishood"
    }

    private val job = Job()

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    private var videoOutputPath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatImageView5.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(github)))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * args
        * */
        val music = arguments?.getInt("music") ?: return
        val videoPath = arguments?.getString("videoPath") ?: return

        /*
        * create an audio file
        * */
        val stream = resources.openRawResource(music)
        val audio = File(context?.filesDir?.path + "/" + "music.mp3")
        audio.createNewFile()
        val fos = FileOutputStream(audio)
        fos.write(stream.readBytes())
        fos.close()

        /*
        * get created audio file path for using on ffmpeg
        * */
        val audioPath = audio.path

        /*
        * get video duration
        * */
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)
        videoOutputPath = "${videoPath}_leo.mp4"
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        val cmd = arrayOf("-i", videoPath, "-i", audioPath, "-ss", "0", "-t", duration, "-c:v", "copy", "-map", "0:v:0", "-map", "1:a:0", "-c:a", "aac", "-shortest", videoOutputPath)

        /*
        * open background thread with Kotlin Coroutines
        * */
        launch {
            val result = FFmpeg.execute(cmd)

            /*
            * delete created audio file after process
            * */
            audio.delete()

            /*
            * run result video on main thread
            * */
            withContext(Dispatchers.Main) {

                videoView.setVideoPath(videoOutputPath)
                videoView.start()
                videoView.setOnCompletionListener { videoView.start() }

                val parent = progressBar2.parent as ViewGroup
                parent.removeView(progressBar2)

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.result_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        /*
        * go to clipart fragment
        * */

        videoOutputPath?.let {
            findNavController().navigate(R.id.action_resultFragment_to_clipartFragment, bundleOf("path" to it))
        }

        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        FFmpeg.cancel()
        job.cancel()
        super.onDestroy()
    }
}