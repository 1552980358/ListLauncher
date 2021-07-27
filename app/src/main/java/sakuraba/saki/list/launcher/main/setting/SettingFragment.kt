package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import lib.github1552980358.ktExtension.androidx.fragment.app.restartActivity
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.dialog.ApplyDialogFragment
import sakuraba.saki.list.launcher.dialog.ResetSettingsDialogFragment
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class SettingFragment: PreferenceFragmentCompat() {
    
    companion object {
        const val BACKGROUND_FILE = "ListLauncherBackground.jpeg"
        
        private const val KEY_SECURITY_SETTING = "key_security_setting"
        private const val KEY_USER_INTERFACE_SETTING = "key_user_interface_setting"
        private const val KEY_QUICK_ACCESS_BUTTON_SETTING = "key_quick_access_button_setting"
        private const val KEY_ABOUT = "key_about"
        private const val KEY_RESTART = "key_restart"
    }
    
    private lateinit var viewModel: SettingViewModel
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        
        initSecurity()
        initUserInterface()
        initQuickAccessButton()
        initAbout()
        initRestart()
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
    
    private fun initQuickAccessButton() =
        findPreference<Preference>(KEY_QUICK_ACCESS_BUTTON_SETTING)?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.nav_quick_access_setting, Bundle().apply { putSerializable(SETTING_CONTAINER, viewModel.settingContainer.value) })
            return@setOnPreferenceClickListener false
        }
    
    private fun initAbout() =
        findPreference<Preference>(KEY_ABOUT)?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.nav_about)
            return@setOnPreferenceClickListener true
        }
    
    private fun initRestart() =
        findPreference<Preference>(KEY_RESTART)?.apply {
            setOnPreferenceClickListener {
                ApplyDialogFragment(object: ApplyDialogFragment.Companion.OnApplyListener {
                    override fun onApply() { restartActivity() }
                }, R.string.setting_restart_content, R.string.setting_restart_confirm).show(parentFragmentManager)
                return@setOnPreferenceClickListener true
            }
        }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_setting, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_reset -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.setting_reset_dialog_title)
                    .setMessage(R.string.setting_reset_dialog_message)
                    .setPositiveButton(R.string.setting_reset_dialog_confirm) { _, _ ->
                        ResetSettingsDialogFragment().show(parentFragmentManager)
                    }.setNegativeButton(R.string.setting_reset_dialog_cancel) { _, _ -> }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
}