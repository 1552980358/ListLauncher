@file:Suppress("DEPRECATION")

package sakuraba.saki.list.launcher.main.launchApp

import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_CANCELED
import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lib.github1552980358.ktExtension.jvm.keyword.tryOnly
import sakuraba.saki.list.launcher.base.Constants
import sakuraba.saki.list.launcher.databinding.FragmentLaunchAppBinding
import sakuraba.saki.list.launcher.main.setting.SettingContainer
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class LaunchAppFragment: Fragment(), FingerprintUtil {
    
    private lateinit var viewModel: LaunchAppViewModel
    private var _fragmentLaunchAppBinding: FragmentLaunchAppBinding? = null
    private val fragmentLaunchAppBinding get() = _fragmentLaunchAppBinding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(LaunchAppViewModel::class.java)
        viewModel.applicationName.observe(this) { appName ->
            fragmentLaunchAppBinding.textViewAppName.text = appName
        }
        viewModel.packageName.observe(this) { pkgName ->
            fragmentLaunchAppBinding.textViewPkgName.text = pkgName
            if (pkgName != null) {
                fragmentLaunchAppBinding.imageViewIcon.setImageDrawable(requireContext().packageManager.getApplicationIcon(pkgName))
            }
        }
        viewModel.setCancellationSignal {
            findNavController().navigateUp()
        }
        _fragmentLaunchAppBinding = FragmentLaunchAppBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return fragmentLaunchAppBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setApplicationName(arguments?.getString(Constants.LAUNCH_APPLICATION_NAME))
        viewModel.setPackageName(arguments?.getString(Constants.LAUNCH_PACKAGE_NAME))
        @Suppress("EXPERIMENTAL_API_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            if ((arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer?)?.getBooleanValue(KEY_USE_FINGERPRINT) == true) {
                fingerprintAuth()
            } else {
                launchApplication()
            }
        }
    }
    
    private fun fingerprintAuth() {
        val fingerprintDialogFragment = FingerprintDialogFragment(viewModel.cancellationSignal.value)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            fingerprintDialogFragment.show(requireActivity().supportFragmentManager)
        }
        if (checkSupportFingerprint(requireContext())) {
            getFingerprintAuth(requireContext(), object : FingerprintAuthCallback {
                override fun success() {
                    launchApplication()
                    if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.M .. Build.VERSION_CODES.O)) {
                        fingerprintDialogFragment.dismiss()
                    }
                }
                override fun failed() {
                    if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.M .. Build.VERSION_CODES.O)) {
                        fingerprintDialogFragment.dismiss()
                    }
                    findNavController().navigateUp()
                }
            
                override fun error(code: Int, message: String?) {
                    if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.M .. Build.VERSION_CODES.O)) {
                        fingerprintDialogFragment.onMessageReceived(message)
                        if (code == FINGERPRINT_ERROR_CANCELED) {
                            fingerprintDialogFragment.dismiss()
                        }
                    }
                    if (code == FINGERPRINT_ERROR_USER_CANCELED) {
                        findNavController().navigateUp()
                    }
                }
            
                override fun exit() {
                    if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.M .. Build.VERSION_CODES.O)) {
                        fingerprintDialogFragment.dismiss()
                    }
                    findNavController().navigateUp()
                }
            }, viewModel.cancellationSignal.value!!)
        }
    }
    
    private fun launchApplication() {
        if (viewModel.packageName.value != null) {
            startActivity(requireContext().packageManager.getLaunchIntentForPackage(viewModel.packageName.value!!))
        }
    }
    
    override fun onDestroyView() {
        viewModel.cancellationSignal.value?.cancel()
        _fragmentLaunchAppBinding = null
        super.onDestroyView()
    }
    
    override fun onPause() {
        findNavController().navigateUp()
        super.onPause()
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}