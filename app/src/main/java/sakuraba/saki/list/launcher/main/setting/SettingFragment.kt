package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.SettingValueChangeListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener.Companion.AUTHORIZATION_LISTENER
import sakuraba.saki.list.launcher.main.launchApp.FingerprintUtil
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_EDIT_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class SettingFragment: PreferenceFragmentCompat(), FingerprintUtil {
    
    companion object {
        const val LAUNCH_TASK = "launch_task"
        const val LAUNCH_TASK_ENABLE = 1
        const val LAUNCH_TASK_MODIFY = 2
    }
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        
        findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.apply {
            if (!checkSupportFingerprint(requireContext())) {
                isSelectable = false
                setSummary(R.string.setting_use_fingerprint_summary_not_available)
            }
            setOnPreferenceClickListener {
                Toast.makeText(requireContext(), R.string.setting_use_fingerprint_wait_for_implement, Toast.LENGTH_SHORT).show()
                return@setOnPreferenceClickListener true
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_FINGERPRINT, newValue as Boolean)
                if (newValue as Boolean) {
                    findPreference<CheckBoxPreference>(KEY_USE_PIN)?.isChecked = true
                }
                return@setOnPreferenceChangeListener true
            }
        }
        
        findPreference<CheckBoxPreference>(KEY_USE_PIN)?.apply {
            if (findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.isChecked == true) {
                if (!sharedPreferences.contains(KEY_PIN_CODE)) {
                    findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.isChecked = false
                } else {
                    if (!isChecked) {
                        isChecked = true
                    }
                }
            }
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    object : SettingValueChangeListener(viewModel.settingContainer.value, KEY_USE_PIN) {
                        override fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: Boolean?) {
                            if (newValue == false) {
                                findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.isChecked = false
                                findPreference<CheckBoxPreference>(KEY_USE_PIN)?.isChecked = false
                            }
                            removeListener()
                        }
                    }
                    findNavController().navigate(R.id.nav_set_pin, Bundle().apply {
                        putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value)
                    })
                } else {
                    findNavController().navigate(R.id.nav_pin_auth, Bundle().apply {
                        putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value)
                        putSerializable(AUTHORIZATION_LISTENER, object : AuthorizationListener {
                            override fun onAuthFailed() {  }
                            override fun onAuthComplete() {
                                if (viewModel.settingContainer.value?.getBooleanValue(KEY_USE_PIN) != false) {
                                    viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_PIN, false)
                                }
                                if (findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.isChecked == true) {
                                    findPreference<CheckBoxPreference>(KEY_USE_FINGERPRINT)?.isChecked = false
                                }
                            }
                        })
                    })
                }
                return@setOnPreferenceChangeListener true
            }
        }
        
        findPreference<Preference>(KEY_EDIT_PIN)?.setOnPreferenceClickListener {
            if (viewModel.settingContainer.value?.getBooleanValue(KEY_USE_PIN) != true ||
                viewModel.settingContainer.value?.getStringValue(KEY_PIN_CODE).isNullOrEmpty()) {
                return@setOnPreferenceClickListener true
            }
            findNavController().navigate(R.id.nav_pin_auth, Bundle().apply {
                putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value)
                putSerializable(AUTHORIZATION_LISTENER, object : AuthorizationListener {
                    override fun onAuthFailed() {  }
                    override fun onAuthComplete() {
                        findNavController().navigate(R.id.nav_set_pin, Bundle().apply {
                            putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value)
                            putInt(LAUNCH_TASK, LAUNCH_TASK_MODIFY)
                        })
                    }
                })
            })
            return@setOnPreferenceClickListener true
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}