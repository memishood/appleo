package tr.com.emrememis.app.leo.ui.music

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_music.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.adapters.MusicAdapter
import tr.com.emrememis.app.leo.data.models.Music
import tr.com.emrememis.app.leo.util.Utils

class MusicFragment : Fragment(), MusicAdapter.MusicAdapterListener {

    private lateinit var adapter: MusicAdapter
    private var mp: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_music, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MusicAdapter.create(this)
        adapter.setItems(Utils.musics)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = adapter
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        layoutManager.orientation = RecyclerView.VERTICAL

        appCompatImageView4.setOnClickListener {
            slide(appCompatImageView2.layoutParams.height, 0)
            releaseMp()
        }

        appCompatImageView3.setOnClickListener {
            when (mp?.isPlaying) {
                true -> {
                    mp?.pause()
                    appCompatImageView3.setImageResource(R.drawable.ic_play)
                }
                false -> {
                    mp?.start()
                    appCompatImageView3.setImageResource(R.drawable.ic_pause)
                }
            }
        }
    }

    override fun onClicked(music: Music) {
        arguments?.let {
            val path = it.getString("videoPath") ?: return@let
            val params = bundleOf("path" to path, "music" to music.imageId)
            findNavController().navigate(R.id.action_musicFragment_to_clipartFragment, params)
        }
    }

    override fun onLongClicked(music: Music) {

        slide(0, appCompatImageView2.layoutParams.height)

        appCompatImageView3.setImageResource(R.drawable.ic_pause)
        appCompatImageView4.setImageResource(R.drawable.ic_close)
        appCompatImageView2.setImageResource(music.image)
        appCompatTextView6.text = music.artist
        appCompatTextView7.text = music.songTitle

        releaseMp()

        mp = MediaPlayer.create(context, Uri.parse("android.resource://${context?.packageName}/${music.imageId}"))
        mp?.start()
    }

    private fun slide(from: Int, to: Int) {
        val animator = ValueAnimator.ofInt(from, to)
        animator.duration = 700

        animator.addUpdateListener { constraintLayout.updateLayoutParams<ConstraintLayout.LayoutParams> { height = it.animatedValue as Int } }

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = BounceInterpolator()
        animatorSet.play(animator)
        animatorSet.start()
    }

    private fun releaseMp() {
        mp?.stop()
        mp?.release()
        mp = null
    }

    override fun onPause() {
        mp?.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mp?.start()
    }

    override fun onStop() {
        releaseMp()
        super.onStop()
    }
}