package sakuraba.saki.list.launcher.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sakuraba.saki.list.launcher.R

class ApplyDialogFragment(
    private val listener: OnApplyListener,
    @StringRes private val message: Int = R.string.apply_dialog_message,
    @StringRes private val positiveButton: Int = R.string.apply_dialog_apply): DialogFragment() {
    
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
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ -> listener.onApply() }
            .setNegativeButton(R.string.apply_dialog_cancel) { _, _ -> listener.onCancel() }
            .create()
    }
    
    fun show(manager: FragmentManager) = show(manager, TAG)
    
}