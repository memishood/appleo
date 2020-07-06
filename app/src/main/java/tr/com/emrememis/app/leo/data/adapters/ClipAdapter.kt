package tr.com.emrememis.app.leo.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_clip.view.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.models.Clip

class ClipAdapter constructor(var cListener: ClipAdapterListener): RecyclerView.Adapter<ClipAdapter.ClipHolder>() {

    private val items = ArrayList<Clip>()

    companion object { fun create(cListener: ClipAdapterListener): ClipAdapter = ClipAdapter(cListener) }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipHolder = ClipHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_clip, parent, false)
    )

    override fun onBindViewHolder(holder: ClipHolder, position: Int) {
        val clip = items[holder.adapterPosition]
        holder.bind(clip)
    }

    fun setItems(clips: List<Clip>) {
        items.clear()
        items.addAll(clips)
        notifyDataSetChanged()
    }

    inner class ClipHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.appCompatImageView6.setOnClickListener {
                val clip = items[adapterPosition]
                cListener.onClicked(clip)
            }
        }

        fun bind(clip: Clip) {
            itemView.appCompatImageView6.setImageResource(clip.imageId)
        }

    }

    interface ClipAdapterListener {
        fun onClicked(clip: Clip)
    }
}