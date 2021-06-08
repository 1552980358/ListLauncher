package sakuraba.saki.list.launcher.main.setting

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT

class SettingFragment: PreferenceFragmentCompat() {
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(arguments?.getSerializable(SettingContainer.SETTING_CONTAINER) as SettingContainer)
        
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_FINGERPRINT, newValue as Boolean)
                return@setOnPreferenceChangeListener true
            }
        }
        
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}