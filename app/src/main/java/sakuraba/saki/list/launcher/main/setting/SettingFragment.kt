package sakuraba.saki.list.launcher.main.setting

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import sakuraba.saki.list.launcher.MainActivity
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.SettingValueChangeListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener.Companion.AUTHORIZATION_LISTENER
import sakuraba.saki.list.launcher.main.launchApp.FingerprintUtil
import sakuraba.saki.list.launcher.main.setting.ColorPickDialogFragment.Companion.OnColorPickListener
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_BLACK_TEXT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_EDIT_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.util.findActivityViewById

class SettingFragment: PreferenceFragmentCompat(), FingerprintUtil {
    
    companion object {
        const val LAUNCH_TASK = "launch_task"
        const val LAUNCH_TASK_ENABLE = 1
        const val LAUNCH_TASK_MODIFY = 2
        
        private const val DEFAULT_STATUS_BAR_COLOR = "#FF3700B3"
        private const val DEFAULT_TOOLBAR_BACKGROUND_COLOR = "#FF6200EE"
    }
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.apply {
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
                    findPreference<SwitchPreferenceCompat>(KEY_USE_PIN)?.isChecked = true
                }
                return@setOnPreferenceChangeListener true
            }
        }
        
        findPreference<SwitchPreferenceCompat>(KEY_USE_PIN)?.apply {
            if (findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked == true) {
                if (!sharedPreferences.contains(KEY_PIN_CODE)) {
                    findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked = false
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
                                findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked = false
                                findPreference<SwitchPreferenceCompat>(KEY_USE_PIN)?.isChecked = false
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
                            override fun onAuthFailed() {
                                Snackbar.make(findActivityViewById(R.id.coordinatorLayout), R.string.pin_authorize_failed_message, Snackbar.LENGTH_SHORT).show()
                            }
                            override fun onAuthComplete() {
                                if (viewModel.settingContainer.value?.getBooleanValue(KEY_USE_PIN) != false) {
                                    viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_PIN, false)
                                }
                                if (findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked == true) {
                                    findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked = false
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
                    override fun onAuthFailed() {
                        Snackbar.make(findActivityViewById(R.id.coordinatorLayout), R.string.pin_authorize_failed_message, Snackbar.LENGTH_SHORT).show()
                    }
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
        
        findPreference<SwitchPreferenceCompat>(KEY_CUSTOM_STATUS_BAR_COLOR)?.apply {
            if (!isChecked) {
                findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.isEnabled = false
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_STATUS_BAR_COLOR, newValue as Boolean)
                if (newValue as Boolean) {
                    setStatusBarColor()
                    findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.isEnabled = true
                } else {
                    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
                    findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.isEnabled = false
                }
                return@setOnPreferenceChangeListener true
            }
        }
        
        findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.setOnPreferenceClickListener {
            setStatusBarColor()
            return@setOnPreferenceClickListener true
        }
        
        findPreference<SwitchPreferenceCompat>(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT)?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT, newValue as Boolean)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
            return@setOnPreferenceChangeListener true
        }
        
        findPreference<SwitchPreferenceCompat>(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR)?.apply {
            if (!preferenceManager.getBoolean(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR, false)) {
                findPreference<Preference>(KEY_TOOLBAR_BACKGROUND_COLOR)?.isEnabled = false
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR, newValue as Boolean)
                if (newValue as Boolean) {
                    findPreference<Preference>(KEY_TOOLBAR_BACKGROUND_COLOR)?.apply {
                        this.isEnabled = true
                        setToolbarBackgroundColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)!!, this)
                    }
                } else {
                    findActivityViewById<AppBarLayout>(R.id.appBarLayout).setBackgroundColor(Color.parseColor(DEFAULT_TOOLBAR_BACKGROUND_COLOR))
                    findPreference<Preference>(KEY_TOOLBAR_BACKGROUND_COLOR)?.isEnabled = false
                }
                return@setOnPreferenceChangeListener true
            }
        }
        
        findPreference<Preference>(KEY_TOOLBAR_BACKGROUND_COLOR)?.apply {
            if (!preferenceManager.contains(KEY_TOOLBAR_BACKGROUND_COLOR)) {
                @Suppress("ApplySharedPref")
                preferenceManager.edit()
                    .putString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)
                    .commit()
            }
            icon.setTint(Color.parseColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, null)))
            setOnPreferenceClickListener {
                setToolbarBackgroundColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)!!, this)
                return@setOnPreferenceClickListener true
            }
        }
        
    }
    
    private fun setStatusBarColor() = ColorPickDialogFragment(object : OnColorPickListener {
            
            override fun onColorPick(color: Int, colorStr: String) {
                viewModel.settingContainer.value?.getStringUpdate(KEY_STATUS_BAR_COLOR, colorStr)
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString(KEY_STATUS_BAR_COLOR, colorStr)
                    .commit()
                requireActivity().window?.statusBarColor = color
            }
            override fun onSelectDefault() {
                viewModel.settingContainer.value?.getStringUpdate(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)
                    .commit()
                requireActivity().window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
            }
            override fun onCancel() { }
        }).show(parentFragmentManager)
    
    private fun setToolbarBackgroundColor(color: String, preference: Preference?) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            viewModel.settingContainer.value?.getStringUpdate(KEY_TOOLBAR_BACKGROUND_COLOR, colorStr)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_TOOLBAR_BACKGROUND_COLOR, colorStr)
                .commit()
            preference?.icon?.setTint(color)
            findActivityViewById<AppBarLayout>(R.id.appBarLayout).setBackgroundColor(color)
        }
        override fun onSelectDefault() {
            viewModel.settingContainer.value?.getStringUpdate(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)
                .commit()
            preference?.icon?.setTint(Color.parseColor(DEFAULT_TOOLBAR_BACKGROUND_COLOR))
            findActivityViewById<AppBarLayout>(R.id.appBarLayout).setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
        }
        override fun onCancel() {
            findActivityViewById<AppBarLayout>(R.id.appBarLayout).setBackgroundColor(Color.parseColor(color))
        }
    }).show(parentFragmentManager)
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}