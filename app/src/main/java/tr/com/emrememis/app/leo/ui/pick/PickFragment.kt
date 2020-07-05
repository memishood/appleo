package tr.com.emrememis.app.leo.ui.pick

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_pick.*
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.ui.permissions.PermissionsFragment
import tr.com.emrememis.app.leo.util.UriToPath

class PickFragment : Fragment() {

    companion object {
        const val PICK_VIDEO_REQUEST_CODE = 713
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_pick, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatButton2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_VIDEO_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data ?: return
        val uri = data.data ?: return
        val path = UriToPath.getFilePath(context, uri) ?: return

        findNavController().navigate(
            R.id.action_pickFragment_to_musicFragment,
            bundleOf("videoPath" to path)
        )

    }

    /*
    * check the permissions if not, go to permissions fragment
    * */
    override fun onResume() {
        super.onResume()
        if (!PermissionsFragment.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            || !PermissionsFragment.hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            findNavController().navigate(R.id.action_pickFragment_to_permissionsFragment)
        }
    }

}