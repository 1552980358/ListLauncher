package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class SettingFragment: PreferenceFragmentCompat() {
    
    companion object {
        const val BACKGROUND_FILE = "ListLauncherBackground.jpeg"
        
        private const val KEY_SECURITY_SETTING = "key_security_setting"
        private const val KEY_USER_INTERFACE_SETTING = "key_user_interface_setting"
    }
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        
        initSecurity()
        initUserInterface()
    }
    
    private fun initSecurity() =
        findPreference<Preference>(KEY_SECURITY_SETTING)?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.nav_security_setting, Bundle().apply { putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value) })
            return@setOnPreferenceClickListener true
        }
    
    private fun initUserInterface() =
        findPreference<Preference>(KEY_USER_INTERFACE_SETTING)?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.nav_user_interface_setting, Bundle().apply { putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value) })
            return@setOnPreferenceClickListener true
        }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}