package sakuraba.saki.list.launcher.ui.home

import android.app.Dialog
import android.app.Service
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sakuraba.saki.list.launcher.databinding.FragmentLoadigDialogBinding

class LoadingDialogFragment: DialogFragment() {
    
    companion object {
        private const val TAG = "LoadingDialogFragment"
    }
    
    private var _fragmentLoadingDialogBinding: FragmentLoadigDialogBinding? = null
    private val fragmentLoadingDialogBinding get() = _fragmentLoadingDialogBinding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        _fragmentLoadingDialogBinding =
            FragmentLoadigDialogBinding
                .inflate(requireContext()
                    .getSystemService(
                        Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                )
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(fragmentLoadingDialogBinding.root)
        }
    }
    
    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
    
}