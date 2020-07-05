package tr.com.emrememis.app.leo.ui.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_permissions.*
import tr.com.emrememis.app.leo.R

class PermissionsFragment : Fragment() {

    companion object {
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 712

        fun hasPermissions(context: Context?, permission: String): Boolean
                = context?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater.inflate(R.layout.fragment_permissions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appCompatButton.setOnClickListener {

            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE
            && hasPermissions(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            && hasPermissions(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            findNavController().navigateUp()
        }
    }

}