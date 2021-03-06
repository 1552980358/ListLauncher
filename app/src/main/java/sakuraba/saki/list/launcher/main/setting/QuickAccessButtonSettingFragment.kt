package sakuraba.saki.list.launcher.main.setting

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import lib.github1552980358.ktExtension.android.content.commit
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.dialog.ColorPickDialogFragment
import sakuraba.saki.list.launcher.dialog.ColorPickDialogFragment.Companion.OnColorPickListener
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_BUTTON_TOUCHED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_COLOR_CLICKED
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_QUICK_ACCESS_ICON_COLOR_NORMAL
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.preference.TextColorChangePreference
import sakuraba.saki.list.launcher.util.updateIconColor

class QuickAccessButtonSettingFragment: PreferenceFragmentCompat() {
    
    companion object {
        private const val DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL = "#FFFFFFFF"
        private const val DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED = "#FFFFFFFF"
        private const val DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_NORMAL = "#FFFFFFFF"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED = "#FFFFFFFF"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL = "#FF6200EE"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED = "#FFBB86FC"
        private const val DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED = "#FFBB86FC"
    }
    
    private var _settingContainer: SettingContainer? = null
    private val settingContainer get() = _settingContainer!!
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_quick_access_setting, rootKey)
        _settingContainer = arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer
        
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        initButtonIconColorNormal(sharedPreference)
        initButtonIconColorClicked(sharedPreference)
        
        initIconBackgroundColorNormal(sharedPreference)
        initIconBackgroundStrokeColorNormal(sharedPreference)
        
        initIconBackgroundColorClicked(sharedPreference)
        initIconBackgroundStrokeColorClicked(sharedPreference)
        
        initButtonNormal(sharedPreference)
        initButtonTouched(sharedPreference)
        
        initButtonBackgroundNormal(sharedPreference)
        initButtonBackgroundStrokeNormal(sharedPreference)
        
        initButtonBackgroundTouched(sharedPreference)
        initButtonBackgroundStrokeTouched(sharedPreference)
    }
    
    private fun initButtonIconColorNormal(sharedPreferences: SharedPreferences) = findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.apply {
        if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)) {
            // @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
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
            // @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
        }
        // icon.setTint(Color.parseColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)))
        updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED))
        setOnPreferenceClickListener {
            setButtonIconColorClicked(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED))
            return@setOnPreferenceClickListener true
        }
    }
    
    private fun initIconBackgroundColorNormal(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)) {
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL))
            setOnPreferenceClickListener {
                setIconBackgroundColorNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun initIconBackgroundStrokeColorNormal(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)) {
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL))
            setOnPreferenceClickListener {
                setIconBackgroundStrokeColorNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun initIconBackgroundColorClicked(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)) {
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED))
            setOnPreferenceClickListener {
                setIconBackgroundColorClicked(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun initIconBackgroundStrokeColorClicked(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)) {
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED))
            setOnPreferenceClickListener {
                setIconBackgroundStrokeColorClicked(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun setButtonIconColorNormal(sharedPreferences: SharedPreferences, colorStr: String?) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.updateIconColor(color)
            @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, colorStr)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, colorStr)
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, colorStr)
        }
        override fun onSelectDefault() {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
            @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_NORMAL, DEFAULT_QUICK_ACCESS_ICON_COLOR_NORMAL)
        }
        override fun onCancel() { }
    }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setButtonIconColorClicked(sharedPreferences: SharedPreferences, colorStr: String?) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)?.updateIconColor(colorStr)
            // @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, colorStr)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, colorStr)
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, colorStr)
        }
        override fun onSelectDefault() {
            findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
            // @Suppress("ApplySharedPref")
            // sharedPreferences.edit()
            //     .putString(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
            //     .commit()
            sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
            settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_COLOR_CLICKED, DEFAULT_QUICK_ACCESS_ICON_COLOR_CLICKED)
        }
        override fun onCancel() { }
    }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setIconBackgroundColorNormal(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)?.updateIconColor(color)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, colorStr)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_NORMAL)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setIconBackgroundStrokeColorNormal(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)?.updateIconColor(color)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, colorStr)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_NORMAL)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setIconBackgroundColorClicked(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)?.updateIconColor(color)
                @Suppress("ApplySharedPref")
                sharedPreferences.edit()
                    .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, colorStr)
                    .commit()
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)
                @Suppress("ApplySharedPref")
                sharedPreferences.edit()
                    .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)
                    .commit()
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_CLICKED)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun setIconBackgroundStrokeColorClicked(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)?.updateIconColor(color)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, colorStr)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)?.updateIconColor(DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)
                // @Suppress("ApplySharedPref")
                // sharedPreferences.edit()
                //     .putString(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)
                //     .commit()
                sharedPreferences.commit(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED, DEFAULT_QUICK_ACCESS_ICON_BACKGROUND_STROKE_CLICKED)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonNormal(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_NORMAL)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_NORMAL)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_NORMAL))
            setOnPreferenceClickListener {
                setButtonNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_NORMAL))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun setButtonNormal(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_NORMAL)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_NORMAL, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_NORMAL, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_NORMAL)?.updateIconColor(KEY_QUICK_ACCESS_BUTTON_NORMAL)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_NORMAL)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_NORMAL)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonTouched(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_TOUCHED)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_TOUCHED)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED))
            setOnPreferenceClickListener {
                setButtonTouched(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun setButtonTouched(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_TOUCHED)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_TOUCHED, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_TOUCHED, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_TOUCHED)?.updateIconColor(DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_TOUCHED)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonBackgroundNormal(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL))
            setOnPreferenceClickListener {
                setButtonBackgroundNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL))
                return@setOnPreferenceClickListener false
            }
        }
    
    private fun setButtonBackgroundNormal(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonBackgroundStrokeNormal(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL))
            setOnPreferenceClickListener {
                setButtonBackgroundStrokeNormal(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun setButtonBackgroundStrokeNormal(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL)?.updateIconColor(DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_NORMAL, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_NORMAL)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonBackgroundTouched(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED))
            setOnPreferenceClickListener {
                setButtonBackgroundTouched(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED))
                return@setOnPreferenceClickListener false
            }
        }
    
    private fun setButtonBackgroundTouched(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)?.updateIconColor(DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_TOUCHED)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
    private fun initButtonBackgroundStrokeTouched(sharedPreferences: SharedPreferences) =
        findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)?.apply {
            if (!sharedPreferences.contains(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)) {
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)
            }
            updateIconColor(sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED))
            setOnPreferenceClickListener {
                setButtonBackgroundStrokeTouched(sharedPreferences, sharedPreferences.getString(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED))
                return@setOnPreferenceClickListener true
            }
        }
    
    private fun setButtonBackgroundStrokeTouched(sharedPreferences: SharedPreferences, colorStr: String?) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)?.updateIconColor(color)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, colorStr)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, colorStr)
            }
            override fun onSelectDefault() {
                findPreference<TextColorChangePreference>(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)?.updateIconColor(DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)
                sharedPreferences.commit(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)
                settingContainer.getStringUpdate(KEY_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED, DEFAULT_QUICK_ACCESS_BUTTON_BACKGROUND_STROKE_TOUCHED)
            }
            override fun onCancel() { }
        }, Color.parseColor(colorStr)).show(parentFragmentManager)
    
}