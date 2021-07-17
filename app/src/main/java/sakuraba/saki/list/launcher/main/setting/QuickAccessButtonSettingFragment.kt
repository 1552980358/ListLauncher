package sakuraba.saki.list.launcher.main.setting

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.dialog.ColorPickDialogFragment
import sakuraba.saki.list.launcher.dialog.ColorPickDialogFragment.Companion.OnColorPickListener
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_COLOR_CLICKED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_COLOR_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.preference.TextColorChangePreference
import sakuraba.saki.list.launcher.util.updateIconColor

class QuickAccessButtonSettingFragment: PreferenceFragmentCompat() {
    
    companion object {
        private const val DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL = "#FFFFFFFF"
        private const val DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED = "#FF6200EE"
    }
    
    private var _settingContainer: SettingContainer? = null
    private val settingContainer get() = _settingContainer!!
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_quick_access_setting, rootKey)
        _settingContainer = arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer
        
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        initButtonIconColorNormal(sharedPreference)
        initButtonIconColorClicked(sharedPreference)
        
    }
    
    private fun initButtonIconColorNormal(sharedPreferences: SharedPreferences) = findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.apply {
        if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)) {
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
                .commit()
        }
        // icon.setTint(Color.parseColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)))
        updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL))
        setOnPreferenceClickListener {
            setButtonIconColorNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL))
            return@setOnPreferenceClickListener true
        }
    }
    
    private fun initButtonIconColorClicked(sharedPreferences: SharedPreferences) = findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)?.apply {
        if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)) {
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
                .commit()
        }
        // icon.setTint(Color.parseColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)))
        updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED))
        setOnPreferenceClickListener {
            setButtonIconColorClicked(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED))
            return@setOnPreferenceClickListener true
        }
    }
    
    private fun setButtonIconColorNormal(sharedPreferences: SharedPreferences, colorStr: String?) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.updateIconColor(color)
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, colorStr)
                .commit()
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, colorStr)
        }
        override fun onSelectDefault() {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
                .commit()
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
        }
        override fun onCancel() { }
    }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setButtonIconColorClicked(sharedPreferences: SharedPreferences, colorStr: String?) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)?.updateIconColor(colorStr)
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, colorStr)
                .commit()
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, colorStr)
        }
        override fun onSelectDefault() {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
            @Suppress("ApplySharedPref")
            sharedPreferences.edit()
                .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
                .commit()
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
        }
        override fun onCancel() { }
    }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
}