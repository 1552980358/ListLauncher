package sakuraba.saki.list.launcher.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sakuraba.saki.list.launcher.R

class ApplyDialogFragment(private val listener: OnApplyListener): DialogFragment() {
    
    companion object {
        interface OnApplyListener {
            fun onApply()
            fun onCancel() {  }
        }
        private const val TAG = "ApplyDialogFragment"
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.apply_dialog_title)
            .setMessage(R.string.apply_dialog_message)
            .setPositiveButton(R.string.apply_dialog_apply) { _, _ -> listener.onApply() }
            .setNegativeButton(R.string.apply_dialog_cancel) { _, _ -> listener.onCancel() }
            .create()
    }
    
    fun show(manager: FragmentManager) = show(manager, TAG)
    
}