package sakuraba.saki.list.launcher.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sakuraba.saki.list.launcher.MainActivity
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentResetSettingDialogBinding
import sakuraba.saki.list.launcher.main.setting.SettingFragment.Companion.BACKGROUND_FILE
import sakuraba.saki.list.launcher.main.setting.getResetSharedPreferenceEditor

class ResetSettingsDialogFragment: DialogFragment() {
    
    companion object {
        private const val TAG = "ResetSettingsDialogFragment"
    }
    
    private var _fragmentResetSettingDialog: FragmentResetSettingDialogBinding? = null
    private val fragmentResetSettingDialogBinding get() = _fragmentResetSettingDialog!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _fragmentResetSettingDialog = FragmentResetSettingDialogBinding.inflate(layoutInflater)
        return super.onCreateDialog(savedInstanceState).apply {
            isCancelable = false
            setContentView(fragmentResetSettingDialogBinding.root)
            CoroutineScope(Dispatchers.IO).launch {
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .getResetSharedPreferenceEditor()
                    .commit()
        
                launch(Dispatchers.Main) { fragmentResetSettingDialogBinding.textView.setText(R.string.reset_setting_dialog_removing_files) }
        
                requireContext().deleteFile(BACKGROUND_FILE)
        
                launch(Dispatchers.Main) { fragmentResetSettingDialogBinding.textView.setText(R.string.reset_setting_dialog_restart_3_second) }
        
                delay(1000)
        
                launch(Dispatchers.Main) { fragmentResetSettingDialogBinding.textView.setText(R.string.reset_setting_dialog_restart_2_second) }
        
                delay(1000)
        
                launch(Dispatchers.Main) { fragmentResetSettingDialogBinding.textView.setText(R.string.reset_setting_dialog_restart_1_second) }
        
                delay(1000)
        
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }
    
    override fun onDestroyView() {
        _fragmentResetSettingDialog = null
        super.onDestroyView()
    }
    
    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
    
}