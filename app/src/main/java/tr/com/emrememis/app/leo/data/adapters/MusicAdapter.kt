package tr.com.emrememis.app.leo.data.adapters

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_music.view.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.models.Music

class MusicAdapter constructor(var mListener: MusicAdapterListener): RecyclerView.Adapter<MusicAdapter.MusicHolder>() {

    companion object { fun create(mListener: MusicAdapterListener): MusicAdapter = MusicAdapter(mListener) }

    private val items = ArrayList<Music>()

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicHolder = MusicHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_music, parent, false)
    )

    override fun onBindViewHolder(holder: MusicHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    fun setItems(musics: List<Music>) {
        items.clear()
        items.addAll(musics)
        notifyDataSetChanged()
    }

    inner class MusicHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.cardView.setOnClickListener {
                val music = items[adapterPosition]
                mListener.onClicked(music)
            }

            itemView.cardView.setOnLongClickListener {
                val vibrator = it.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if (Build.VERSION.SDK_INT >= 26)
                    vibrator.vibrate(VibrationEffect.createOneShot(200L, VibrationEffect.DEFAULT_AMPLITUDE))
                else
                    vibrator.vibrate(200L)

                val music = items[adapterPosition]
                mListener.onLongClicked(music)

                true
            }
        }

        fun bind(music: Music) {
            itemView.appCompatTextView4.text = music.artist
            itemView.appCompatTextView5.text = music.songTitle
            itemView.appCompatImageView.setImageResource(music.image)
        }

    }

    interface MusicAdapterListener {
        fun onClicked(music: Music)
        fun onLongClicked(music: Music)
    }
}