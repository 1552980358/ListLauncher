package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.booleanSettingValueChangeListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener
import sakuraba.saki.list.launcher.main.launchApp.FingerprintUtil
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.preference.TwoSidedSwitchPreferenceCompat
import sakuraba.saki.list.launcher.util.findActivityViewById

class SecuritySettingFragment: PreferenceFragmentCompat(), FingerprintUtil {
    
    companion object {
        const val LAUNCH_TASK = "launch_task"
        const val LAUNCH_TASK_ENABLE = 1
        const val LAUNCH_TASK_MODIFY = 2
    }
    
    private var _settingContainer: SettingContainer? = null
    private val settingContainer get() = _settingContainer!!
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_security_setting, rootKey)
        _settingContainer = arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer
        
        initFingerprint()
        initPinCode()
    }
    
    private fun initFingerprint() =
        findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.apply {
            if (!checkSupportFingerprint(requireContext())) {
                isEnabled = false
                setSummary(R.string.setting_use_fingerprint_summary_not_available)
            }
            setOnPreferenceChangeListener { _, newValue ->
                settingContainer.getBooleanUpdate(KEY_USE_FINGERPRINT, newValue as Boolean)
                if (newValue) {
                    findPreference<SwitchPreferenceCompat>(KEY_USE_PIN)?.isChecked = true
                }
                return@setOnPreferenceChangeListener true
            }
        }
    
    private fun initPinCode() =
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_USE_PIN)?.apply {
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
                    @Suppress("NAME_SHADOWING")
                    booleanSettingValueChangeListener(settingContainer, KEY_USE_PIN) { _, _, newValue ->
                        if (newValue == false) {
                            findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.isChecked = false
                            findPreference<SwitchPreferenceCompat>(KEY_USE_PIN)?.isChecked = false
                        }
                        removeListener()
                    }
                    findNavController().navigate(R.id.nav_set_pin, Bundle().apply {
                        putSerializable(SETTING_CONTAINER, settingContainer)
                    })
                } else {
                    findNavController().navigate(R.id.nav_pin_auth, Bundle().apply {
                        putSerializable(SETTING_CONTAINER, settingContainer)
                        putSerializable(AuthorizationListener.AUTHORIZATION_LISTENER, object : AuthorizationListener {
                            override fun onAuthFailed() {
                                Snackbar.make(findActivityViewById(R.id.coordinatorLayout), R.string.pin_authorize_failed_message, Snackbar.LENGTH_SHORT).show()
                            }
                            override fun onAuthComplete() {
                                if (settingContainer.getBooleanValue(KEY_USE_PIN) != false) {
                                    settingContainer.getBooleanUpdate(KEY_USE_PIN, false)
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
            setOnContentClickListener {
                if (sharedPreferences.contains(KEY_PIN_CODE) && settingContainer.getBooleanValue(KEY_USE_PIN) == true) {
                    findNavController().navigate(R.id.nav_pin_auth, Bundle().apply {
                        putSerializable(SETTING_CONTAINER, settingContainer)
                        putSerializable(AuthorizationListener.AUTHORIZATION_LISTENER, object : AuthorizationListener {
                            override fun onAuthFailed() {
                                Snackbar.make(findActivityViewById(R.id.coordinatorLayout), R.string.pin_authorize_failed_message, Snackbar.LENGTH_SHORT).show()
                            }
                            override fun onAuthComplete() {
                                findNavController().navigate(R.id.nav_set_pin, Bundle().apply {
                                    putSerializable(SETTING_CONTAINER, settingContainer)
                                    putInt(LAUNCH_TASK, LAUNCH_TASK_MODIFY)
                                })
                            }
                        })
                    })
                } else {
                    Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_use_pin_should_enable_to_set,
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    
}