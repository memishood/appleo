package tr.com.emrememis.app.leo.ui.clipart

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_clipart.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.data.adapters.ClipAdapter
import tr.com.emrememis.app.leo.data.models.Clip
import tr.com.emrememis.app.leo.util.Utils

class ClipartFragment : Fragment(), ClipAdapter.ClipAdapterListener {

    private lateinit var adapter: ClipAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clipart, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ClipAdapter.create(this)
        adapter.setItems(Utils.clips)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView2.adapter = adapter
    }

    override fun onClicked(clip: Clip) {

        arguments?.let {
            val path = it.getString("path") ?: return@let
            val music = it.getInt("music")

            val params = bundleOf("path" to path, "music" to music, "clip" to clip.imageId)
            findNavController().navigate(R.id.action_clipartFragment_to_resultFragment, params)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.clip_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        arguments?.let {
            val path = it.getString("path") ?: return@let
            val music = it.getInt("music")

            val params = bundleOf("path" to path, "music" to music)
            findNavController().navigate(R.id.action_clipartFragment_to_resultFragment, params)

        }

        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}