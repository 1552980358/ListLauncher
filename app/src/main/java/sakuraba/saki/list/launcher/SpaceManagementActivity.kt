package sakuraba.saki.list.launcher

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import sakuraba.saki.list.launcher.dialog.ResetSettingsDialogFragment

class SpaceManagementActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        AlertDialog.Builder(this)
            .setTitle(R.string.space_management_dialog_title)
            .setMessage(R.string.space_management_dialog_message)
            .setPositiveButton(R.string.space_management_dialog_confirm) { _, _ ->
                ResetSettingsDialogFragment().showInSystemSetting(supportFragmentManager)
            }.setNegativeButton(R.string.space_management_dialog_cancel) { _, _ -> }
            .show()
        
    }
    
}