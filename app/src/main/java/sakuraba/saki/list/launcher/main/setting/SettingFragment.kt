package sakuraba.saki.list.launcher.main.setting

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import sakuraba.saki.list.launcher.MainActivity
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.SettingValueChangeListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener.Companion.AUTHORIZATION_LISTENER
import sakuraba.saki.list.launcher.main.launchApp.FingerprintUtil
import sakuraba.saki.list.launcher.main.setting.ColorPickDialogFragment.Companion.OnColorPickListener
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_BACKGROUND_IMAGE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_BACKGROUND_IMAGE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_NAVIGATION_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_BLACK_TEXT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_SUMMARY_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_TITLE_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_NAVIGATION_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_SUMMARY_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_TITLE_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.preference.TwoSidedSwitchPreferenceCompat
import sakuraba.saki.list.launcher.util.findActivityViewById
import sakuraba.saki.list.launcher.util.hasNavigationBar
import sakuraba.saki.list.launcher.view.CropImageView

class SettingFragment: PreferenceFragmentCompat(), FingerprintUtil {
    
    companion object {
        const val LAUNCH_TASK = "launch_task"
        const val LAUNCH_TASK_ENABLE = 1
        const val LAUNCH_TASK_MODIFY = 2
        
        private const val DEFAULT_STATUS_BAR_COLOR = "#FF3700B3"
        private const val DEFAULT_TOOLBAR_BACKGROUND_COLOR = "#FF6200EE"
        private const val DEFAULT_TITLE_COLOR = "#FF000000"
        private const val DEFAULT_SUMMARY_COLOR = "#FF757575"
        private const val DEFAULT_NAVIGATION_BAR_COLOR = "#00FFFFFF"
        
        const val BACKGROUND_FILE = "ListLauncherBackground.jpeg"
        
        const val CROP_URI = "crop_uri"
    }
    
    private lateinit var viewModel: SettingViewModel
    // private lateinit var cropImageUri: Uri
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_setting, rootKey)
        
        setHasOptionsMenu(true)
        
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(requireContext())
        
        initFingerprint()
        initPinCode()
        initStatusBarColor(preferenceManager)
        initStatusBarTextColor()
        initToolbarBackgroundColor(preferenceManager)
        initBackground(preferenceManager)
        initTitleColor(preferenceManager)
        initSummaryColor(preferenceManager)
        initTransparentNavigationBar(preferenceManager)
    }
    
    private fun initFingerprint() =
        findPreference<SwitchPreferenceCompat>(KEY_USE_FINGERPRINT)?.apply {
            if (!checkSupportFingerprint(requireContext())) {
                isEnabled = false
                setSummary(R.string.setting_use_fingerprint_summary_not_available)
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_USE_FINGERPRINT, newValue as Boolean)
                if (newValue as Boolean) {
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
        setOnContentClickListener {
            if (sharedPreferences.contains(KEY_PIN_CODE) && viewModel.settingContainer.value?.getBooleanValue(KEY_USE_PIN) == true) {
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
            } else {
                Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_use_pin_should_enable_to_set, LENGTH_SHORT).show()
            }
        }
    }
    
    private fun initStatusBarColor(preferenceManager: SharedPreferences) =
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_STATUS_BAR_COLOR)?.apply {
            if (!preferenceManager.contains(KEY_STATUS_BAR_COLOR)) {
                @Suppress("ApplySharedPref")
                preferenceManager.edit()
                    .putString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)
                    .commit()
            }
            icon.setTint(Color.parseColor(preferenceManager.getString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)!!))
            setOnContentClickListener {
                if (preferenceManager.getBoolean(KEY_CUSTOM_STATUS_BAR_COLOR, false)) {
                    setStatusBarColor(preferenceManager.getString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)!!)
                } else {
                    Snackbar.make(
                        findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                        R.string.setting_status_bar_snackbar_should_enable_to_set,
                        LENGTH_SHORT
                    ).show()
                }
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_STATUS_BAR_COLOR, newValue as Boolean)
                if (newValue as Boolean) {
                    setStatusBarColor(preferenceManager.getString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)!!)
                    findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.isEnabled = true
                } else {
                    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
                    findPreference<Preference>(KEY_STATUS_BAR_COLOR)?.isEnabled = false
                }
                return@setOnPreferenceChangeListener true
            }
        }
    
    private fun initStatusBarTextColor() =
        findPreference<SwitchPreferenceCompat>(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT)?.setOnPreferenceChangeListener { _, newValue ->
        viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT, newValue as Boolean)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
        return@setOnPreferenceChangeListener true
    }
    
    private fun initToolbarBackgroundColor(preferenceManager: SharedPreferences) =
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR)?.apply {
        if (!preferenceManager.contains(KEY_TOOLBAR_BACKGROUND_COLOR)) {
            @Suppress("ApplySharedPref")
            preferenceManager.edit()
                .putString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)
                .commit()
        }
        icon.setTint(Color.parseColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, null)))
        setOnContentClickListener {
            if (preferenceManager.getBoolean(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR, false)) {
                setToolbarBackgroundColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)!!, this)
            } else {
                Snackbar.make(
                    findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                    R.string.setting_toolbar_bg_snackbar_should_enable_to_set,
                    LENGTH_SHORT
                ).show()
            }
        }
        setOnPreferenceChangeListener { _, newValue ->
            viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR, newValue as Boolean)
            if (newValue as Boolean) {
                setToolbarBackgroundColor(preferenceManager.getString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)!!, this)
            } else {
                findActivityViewById<AppBarLayout>(R.id.appBarLayout).setBackgroundColor(Color.parseColor(DEFAULT_TOOLBAR_BACKGROUND_COLOR))
                findPreference<Preference>(KEY_TOOLBAR_BACKGROUND_COLOR)?.isEnabled = false
            }
            return@setOnPreferenceChangeListener true
        }
    }
    
    private fun initBackground(preferenceManager: SharedPreferences) {
        /**
         * Due to the limitation of Android Q [Build.VERSION_CODES.Q], application directories,
         * including the external directory fetched with application-owned [Context], e.g. [Context.getExternalFilesDir],
         * the cropping of image is handled by external application, that there is limitation on the access of the file.
         * For fetching the cropped file, we can only use the public directory to store and access the cropped image.
         *
         * 用中文简单的说一下，反正就是[Context]限制的，一切用[Context]获取的路径，包括[Context.getExternalFilesDir]，都是应用专属，
         * 使用com.android.camera.action.CROP会调用非本应用处理，由于[Context]的限制，导致无法保存。所以只能用公共空间来保存访问了。
         *
         * This solution is suggested by
         * 解决方案是来自 [https://blog.csdn.net/qq_35584878/article/details/115284323]
         *
         * No need, cropping will be handled by [CropImageView] in the [CropImageFragment]
         *
         * // @Suppress("DEPRECATION")
         * // cropImageUri = Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), BACKGROUND_FILE))
         **/
        val getImageContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            when (activityResult.resultCode) {
                RESULT_OK -> {
                    findNavController().navigate(R.id.nav_crop_image, Bundle().apply { putString(CROP_URI, activityResult.data?.data?.toString()) })
                }
                else -> {
                    Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_background_snackbar_fetching_image_fail, LENGTH_SHORT).show()
                }
            }
        }
    
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_BACKGROUND_IMAGE)?.apply {
            setOnContentClickListener {
                if (preferenceManager.getBoolean(KEY_CUSTOM_BACKGROUND_IMAGE, false)) {
                    getImageContent.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
                } else {
                    Snackbar.make(
                        findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                        R.string.setting_background_snackbar_should_enable_to_set,
                        LENGTH_SHORT
                    ).show()
                }
            }
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.settingContainer.value?.getBooleanUpdate(KEY_CUSTOM_BACKGROUND_IMAGE, newValue as Boolean)
                findPreference<Preference>(KEY_BACKGROUND_IMAGE)?.isEnabled = newValue as Boolean
                if (newValue) {
                    getImageContent.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
                } else {
                    findActivityViewById<DrawerLayout>(R.id.drawer_layout).background = null
                }
                return@setOnPreferenceChangeListener true
            }
        }
    }
    
    private fun initTitleColor(preferenceManager: SharedPreferences) =
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_TITLE_COLOR)?.apply {
            if (!preferenceManager.contains(KEY_TITLE_COLOR)) {
                @Suppress("ApplySharedPref")
                preferenceManager.edit()
                    .putString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)
                    .commit()
            }
            icon.setTint(Color.parseColor(preferenceManager.getString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)))
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    setTitleTextColor(preferenceManager.getString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)!!)
                } else {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                return@setOnPreferenceChangeListener true
            }
            setOnContentClickListener {
                if (preferenceManager.getBoolean(KEY_CUSTOM_TITLE_COLOR, false)) {
                    setTitleTextColor(preferenceManager.getString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)!!)
                } else {
                    Snackbar.make(
                        findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                        R.string.setting_custom_title_color_snackbar_should_enable_to_set,
                        LENGTH_SHORT
                    ).show()
                }
            }
        }
    
    private fun initSummaryColor(preferenceManager: SharedPreferences) =
        findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_SUMMARY_COLOR)?.apply {
            if (!preferenceManager.contains(KEY_SUMMARY_COLOR)) {
                @Suppress("ApplySharedPref")
                preferenceManager.edit()
                    .putString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)
                    .commit()
            }
            icon.setTint(Color.parseColor(preferenceManager.getString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)))
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    setSummaryTextColor(preferenceManager.getString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)!!)
                } else {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
                return@setOnPreferenceChangeListener true
            }
            setOnContentClickListener {
                if (preferenceManager.getBoolean(KEY_CUSTOM_SUMMARY_COLOR, false)) {
                    setSummaryTextColor(preferenceManager.getString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)!!)
                } else {
                    Snackbar.make(
                        findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                        R.string.setting_custom_summary_color_snackbar_should_enable_to_set,
                        LENGTH_SHORT
                    ).show()
                }
            }
        }
    
    private fun initTransparentNavigationBar(preferenceManager: SharedPreferences) = findPreference<TwoSidedSwitchPreferenceCompat>(KEY_CUSTOM_NAVIGATION_BAR_COLOR)?.apply {
        if (!hasNavigationBar) {
            isEnabled = false
            setSummary(R.string.setting_custom_navigation_bar_not_supported)
            return@apply
        }
        if (!preferenceManager.contains(KEY_NAVIGATION_BAR_COLOR)) {
            @Suppress("ApplySharedPref")
            preferenceManager.edit()
                .putString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)
                .commit()
        }
        icon.setTint(Color.parseColor(preferenceManager.getString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)))
        setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                setNavigationBarColor(preferenceManager.getString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)!!)
            } else {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            return@setOnPreferenceChangeListener true
        }
        setOnContentClickListener {
            if (preferenceManager.getBoolean(KEY_CUSTOM_NAVIGATION_BAR_COLOR, false)) {
                setNavigationBarColor(preferenceManager.getString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)!!)
            } else {
                Snackbar.make(
                    findActivityViewById<DrawerLayout>(R.id.drawer_layout),
                    R.string.setting_custom_navigation_bar_should_enable_to_set,
                    LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun setStatusBarColor(color: String) = ColorPickDialogFragment(object : OnColorPickListener {
            
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
        }, Color.parseColor(color)).show(parentFragmentManager)
    
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
    }, Color.parseColor(color)).show(parentFragmentManager)
    
    private fun setTitleTextColor(color: String) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            viewModel.settingContainer.value?.getStringUpdate(KEY_TITLE_COLOR, colorStr)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_TITLE_COLOR, colorStr)
                .commit()
            // preference.icon?.setTint(color)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    
        override fun onSelectDefault() {
            viewModel.settingContainer.value?.getStringUpdate(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)
                .commit()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    
        override fun onCancel() {}
    }, Color.parseColor(color)).show(parentFragmentManager)
    
    private fun setSummaryTextColor(color: String) =
        ColorPickDialogFragment(object : OnColorPickListener {
            override fun onColorPick(color: Int, colorStr: String) {
                viewModel.settingContainer.value?.getStringUpdate(KEY_SUMMARY_COLOR, colorStr)
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString(KEY_SUMMARY_COLOR, colorStr)
                    .commit()
                // preference.icon?.setTint(color)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        
            override fun onSelectDefault() {
                viewModel.settingContainer.value?.getStringUpdate(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)
                    .commit()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
        
            override fun onCancel() {}
        }, Color.parseColor(color)).show(parentFragmentManager)
    
    private fun setNavigationBarColor(colorString: String) = ColorPickDialogFragment(object : OnColorPickListener {
        override fun onColorPick(color: Int, colorStr: String) {
            viewModel.settingContainer.value?.getStringUpdate(KEY_NAVIGATION_BAR_COLOR, colorStr)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_NAVIGATION_BAR_COLOR, colorStr)
                .commit()
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            requireActivity().window.navigationBarColor = Color.parseColor(colorString)
        }
    
        override fun onSelectDefault() {
            viewModel.settingContainer.value?.getStringUpdate(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)
            @Suppress("ApplySharedPref")
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .edit()
                .putString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)
                .commit()
            WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
            requireActivity().window.navigationBarColor = Color.parseColor(DEFAULT_NAVIGATION_BAR_COLOR)
        }
    
        override fun onCancel() {}
    
    }, Color.parseColor(colorString)).show(parentFragmentManager)
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}