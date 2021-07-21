package sakuraba.saki.list.launcher

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import lib.github1552980358.ktExtension.jvm.keyword.tryOnly
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver.Companion.APPLICATION_CHANGE_BROADCAST_RECEIVER
import sakuraba.saki.list.launcher.databinding.ActivityMainBinding
import sakuraba.saki.list.launcher.main.MainViewModel
import sakuraba.saki.list.launcher.main.home.TimeBroadcastReceiver
import sakuraba.saki.list.launcher.main.setting.SettingContainer
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_BACKGROUND_IMAGE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_NAVIGATION_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_BLACK_TEXT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_SUMMARY_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_TITLE_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_NAVIGATION_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_SUMMARY_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_TITLE_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_TOOLBAR_BACKGROUND_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_SYSTEM_BACKGROUND
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_TOOLBAR_LIGHT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.main.setting.SettingFragment.Companion.BACKGROUND_FILE
import sakuraba.saki.list.launcher.view.base.TextViewInterface

class MainActivity: AppCompatActivity(), TextViewInterface {
    
    private lateinit var viewModel: MainViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private val activityMainBinding get() = _activityMainBinding!!
    
    private val timeBroadcastReceiver = TimeBroadcastReceiver()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setSettingContainer(SettingContainer(this))
        viewModel.setApplicationChangeBroadcastReceiver(ApplicationChangeBroadcastReceiver(this))
    
        intent!!.putExtra(SETTING_CONTAINER, viewModel.settingContainer.value)
        intent.putExtra(APPLICATION_CHANGE_BROADCAST_RECEIVER, viewModel.applicationChangeBroadcastReceiver.value)
    
        getCustomizeSystem()
        
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        getCustomActivitySet()
        // setContentView(R.layout.activity_main)
        setContentView(activityMainBinding.root)
        // val toolbar: Toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)
        setSupportActionBar(activityMainBinding.root.findViewById(R.id.toolbar))
        
        // val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        // val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), activityMainBinding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        activityMainBinding.navView.setupWithNavController(navController)
        activityMainBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                if (!timeBroadcastReceiver.hasInitialized) {
                    timeBroadcastReceiver.setTextView(drawerView.findViewById(R.id.text_view_time), drawerView.findViewById(R.id.text_view_date))
                }
                timeBroadcastReceiver.getTimeInit()
            }
    
            override fun onDrawerOpened(drawerView: View) {
                timeBroadcastReceiver.getRegister(this@MainActivity)
            }
    
            override fun onDrawerClosed(drawerView: View) {
                timeBroadcastReceiver.getUnregister(this@MainActivity)
                timeBroadcastReceiver.setTextView()
            }
    
            override fun onDrawerStateChanged(newState: Int) {
            
            }
        })
    }
    
    private fun getCustomizeSystem() {
        viewModel.settingContainer.value?.apply {
            if (getBooleanValue(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT) == true) {
                when  {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        setTheme(R.style.Theme_ListLauncher_LightStatusBar)
                    }
                    else -> {
                        @Suppress("DEPRECATION", "InlinedApi")
                        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
            if (getBooleanValue(KEY_CUSTOM_STATUS_BAR_COLOR) == true
                && getStringValue(KEY_STATUS_BAR_COLOR) != null) {
                window.statusBarColor = Color.parseColor(getStringValue(KEY_STATUS_BAR_COLOR))
            }
            if (getBooleanValue(KEY_CUSTOM_NAVIGATION_BAR_COLOR) == true) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.navigationBarColor = Color.parseColor(getStringValue(KEY_NAVIGATION_BAR_COLOR))
            }
        }
    }
    
    private fun getCustomActivitySet() {
        viewModel.settingContainer.value?.apply {
            if (getBooleanValue(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR) == true) {
                activityMainBinding.root
                    .findViewById<AppBarLayout>(R.id.appBarLayout)
                    .setBackgroundColor(Color.parseColor(getStringValue(KEY_TOOLBAR_BACKGROUND_COLOR)!!))
            }
            if (getBooleanValue(KEY_CUSTOM_BACKGROUND_IMAGE) == true) {
                tryOnly {
                    activityMainBinding.drawerLayout.background =
                        BitmapFactory.decodeStream(openFileInput(BACKGROUND_FILE))?.toDrawable(resources)
                }
            } else if (getBooleanValue(KEY_USE_SYSTEM_BACKGROUND) == true &&
                ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    activityMainBinding.drawerLayout.background = WallpaperManager.getInstance(this@MainActivity).drawable
            }
            /**
             * Due to [AppBarLayout] does not provide setting style after instance was created.
             **/
            activityMainBinding.root.addView(
                layoutInflater.inflate(
                    if (getBooleanValue(KEY_USE_TOOLBAR_LIGHT) == true) R.layout.app_bar_main_light else R.layout.app_bar_main,
                    activityMainBinding.root,
                    false
                )
            )
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    override fun onDestroy() {
        viewModel.applicationChangeBroadcastReceiver.value?.getUnregistered(this)
        _activityMainBinding = null
        super.onDestroy()
    }
    
    override fun hasCustomTitleColor(): Boolean {
        return viewModel.settingContainer.value?.getBooleanValue(KEY_CUSTOM_TITLE_COLOR) == true
    }
    
    override fun getTitleTextColor() = try {
        Color.parseColor(viewModel.settingContainer.value?.getStringValue(KEY_TITLE_COLOR))
    } catch (e: Exception) {
        Color.BLACK
    }
    
    override fun hasCustomSummaryColor(): Boolean {
        return viewModel.settingContainer.value?.getBooleanValue(KEY_CUSTOM_SUMMARY_COLOR) == true
    }
    
    override fun getSummaryTextColor()= try {
        Color.parseColor(viewModel.settingContainer.value?.getStringValue(KEY_SUMMARY_COLOR))
    } catch (e: Exception) {
        Color.BLACK
    }
    
    override fun onBackPressed() {
        if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.nav_home) {
            super.onBackPressed()
        }
    }
    
}