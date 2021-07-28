package sakuraba.saki.list.launcher.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.main.home.RecyclerViewAdapter.Companion.APP_INFO
import sakuraba.saki.list.launcher.preference.ApplicationIconPreference

class ApplicationInfoFragment: PreferenceFragmentCompat() {
    
    companion object {
        private const val KEY_APPLICATION_ICON = "key_application_icon"
        private const val KEY_APPLICATION_NAME = "key_application_name"
        private const val KEY_PACKAGE_NAME = "key_package_name"
        private const val KEY_INSTALLATION_TYPE = "key_installation_type"
        private const val KEY_VERSION_NAME = "key_version_name"
        private const val KEY_VERSION_CODE = "key_version_code"
        private const val KEY_SYSTEM_SETTING = "key_system_setting"
    }
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_application_info, rootKey)
        
        val appInfo = arguments?.getSerializable(APP_INFO) as AppInfo
        findPreference<ApplicationIconPreference>(KEY_APPLICATION_ICON)?.setApplicationIcon(appInfo.icon)
        findPreference<Preference>(KEY_APPLICATION_NAME)?.summary = appInfo.name
        findPreference<Preference>(KEY_PACKAGE_NAME)?.summary = appInfo.packageName
        findPreference<Preference>(KEY_INSTALLATION_TYPE)?.setSummary(
            when {
                appInfo.isSystem -> R.string.application_info_system_application
                else -> R.string.application_info_user_installed
            }
        )
        findPreference<Preference>(KEY_VERSION_NAME)?.summary = appInfo.versionName
        findPreference<Preference>(KEY_VERSION_CODE)?.summary = appInfo.versionCode.toString()
        findPreference<Preference>(KEY_SYSTEM_SETTING)?.apply {
            setTitle(R.string.application_info_setting_info)
            setOnPreferenceClickListener {
                requireContext().startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", appInfo.packageName, null)))
                findNavController().navigateUp()
                return@setOnPreferenceClickListener true
            }
        }
    }
}