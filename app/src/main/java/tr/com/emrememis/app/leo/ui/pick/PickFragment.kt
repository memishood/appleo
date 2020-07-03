package tr.com.emrememis.app.leo.ui.pick

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import tr.com.emrememis.app.leo.R
import tr.com.emrememis.app.leo.ui.permissions.PermissionsFragment

class PickFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_pick, container, false)

    override fun onResume() {
        super.onResume()
        if (!PermissionsFragment.hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            findNavController().navigate(R.id.action_pickFragment_to_permissionsFragment)
        }
    }

}