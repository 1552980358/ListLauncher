package sakuraba.saki.list.launcher.main.launchApp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentFingerprintDialogBinding

class FingerprintDialogFragment(private val cancellationSignal: CancellationSignal?): DialogFragment() {
    
    companion object {
        private const val TAG = "FingerprintDialogFragment"
    }
    
    private var _fragmentFingerprintDialogBinding: FragmentFingerprintDialogBinding? = null
    private val fragmentFingerprintDialogBinding get() = _fragmentFingerprintDialogBinding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _fragmentFingerprintDialogBinding = FragmentFingerprintDialogBinding.inflate(layoutInflater)
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.fingerprint_auth_title)
            .setNegativeButton(R.string.fingerprint_auth_cancel) { _, _ ->
                cancellationSignal!!.cancel()
            }
            .setOnCancelListener {
                cancellationSignal!!.cancel()
            }
            .setView(fragmentFingerprintDialogBinding.root)
            .show()
    }
    
    fun onMessageReceived(newMessage: String?) {
        fragmentFingerprintDialogBinding.textView.apply {
            post { text = newMessage }
        }
    }
    
    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
    
}