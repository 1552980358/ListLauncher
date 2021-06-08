package sakuraba.saki.list.launcher.main.setting

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.main.launchApp.FingerprintUtil
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT

class SettingFragment: PreferenceFragmentCompat(), FingerprintUtil {
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(arguments?.getSerializable(SettingContainer.SETTING_CONTAINER) as SettingContainer)
        
        findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.apply {
            if (!checkSupportFingerprint(requireContext())) {
                isSelectable = false
                setSummary(R.string.setting_use_fingerprint_summary_not_available)
            }
            setOnPreferenceClickListener {
                Toast.makeText(requireContext(), R.string.setting_use_fingerprint_wait_for_implement, Toast.LENGTH_SHORT).show()
                return@setOnPreferenceClickListener false
            }
            setOnPreferenceChangeListener { _, newValue ->
                // viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_FINGERPRINT, newValue as Boolean)
                return@setOnPreferenceChangeListener false
            }
        }
        
        
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}