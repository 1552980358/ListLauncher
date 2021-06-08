package sakuraba.saki.list.launcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import sakuraba.saki.list.launcher.base.Constants.Companion.LAUNCH_APPLICATION_NAME
import sakuraba.saki.list.launcher.base.Constants.Companion.LAUNCH_PACKAGE_NAME
import sakuraba.saki.list.launcher.databinding.ActivityLaunchAppBinding
import sakuraba.saki.list.launcher.launchApp.LaunchAppViewModel

class LaunchAppActivity: AppCompatActivity() {
    
    private lateinit var viewModel: LaunchAppViewModel
    private var _activityLaunchAppBinding: ActivityLaunchAppBinding? = null
    private val activityLaunchAppBinding get() = _activityLaunchAppBinding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLaunchAppBinding = ActivityLaunchAppBinding.inflate(layoutInflater)
        setContentView(activityLaunchAppBinding.root)
        viewModel = ViewModelProvider(this).get(LaunchAppViewModel::class.java)
        viewModel.applicationName.observe(this) { applicationName: String? ->
            activityLaunchAppBinding.textViewAppName.text = applicationName
        }
        viewModel.packageName.observe(this) { packageName: String? ->
            activityLaunchAppBinding.textViewPkgName.text = packageName
            if (packageName == null) {
                activityLaunchAppBinding.imageViewIcon.setImageDrawable(null)
            } else {
                activityLaunchAppBinding.imageViewIcon.setImageDrawable(packageManager.getApplicationIcon(packageName))
            }
        }
        
        viewModel.setApplicationName(intent.getStringExtra(LAUNCH_APPLICATION_NAME))
        viewModel.setPackageName(intent.getStringExtra(LAUNCH_PACKAGE_NAME))
    
        if (viewModel.packageName.value != null) {
            startActivity(packageManager.getLaunchIntentForPackage(viewModel.packageName.value!!))
        }
        finish()
    }
    
    override fun onDestroy() {
        _activityLaunchAppBinding = null
        super.onDestroy()
    }

}