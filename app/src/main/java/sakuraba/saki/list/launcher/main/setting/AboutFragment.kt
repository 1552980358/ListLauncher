package sakuraba.saki.list.launcher.main.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sakuraba.saki.list.launcher.BuildConfig
import sakuraba.saki.list.launcher.R

class AboutFragment: PreferenceFragmentCompat() {
    
    companion object {
        private const val KEY_VERSION_NAME = "key_version_name"
        private const val KEY_VERSION_CODE = "key_version_code"
        private const val KEY_BUILD_TYPE = "key_build_type"
        private const val KEY_LICENSE = "key_license"
        private const val KEY_PROJECT_GITHUB = "key_project_github"
    }
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_about, rootKey)
        
        findPreference<Preference>(KEY_VERSION_NAME)?.summary = BuildConfig.VERSION_NAME
        findPreference<Preference>(KEY_VERSION_CODE)?.summary = BuildConfig.VERSION_CODE.toString()
        findPreference<Preference>(KEY_BUILD_TYPE)?.summary = BuildConfig.BUILD_TYPE
        findPreference<Preference>(KEY_LICENSE)?.setOnPreferenceClickListener { _ ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/1552980358/ListLauncher/blob/master/LICENSE")))
            return@setOnPreferenceClickListener true
        }
        findPreference<Preference>(KEY_PROJECT_GITHUB)?.setOnPreferenceClickListener { _ ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/1552980358/ListLauncher")))
            return@setOnPreferenceClickListener true
        }
    }
    
}