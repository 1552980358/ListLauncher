package sakuraba.saki.list.launcher.main.launchApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sakuraba.saki.list.launcher.base.Constants
import sakuraba.saki.list.launcher.databinding.FragmentLaunchAppBinding

class LaunchAppFragment: Fragment() {
    
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
        _fragmentLaunchAppBinding = FragmentLaunchAppBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return fragmentLaunchAppBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setApplicationName(arguments?.getString(Constants.LAUNCH_APPLICATION_NAME))
        viewModel.setPackageName(arguments?.getString(Constants.LAUNCH_PACKAGE_NAME))
        if (viewModel.packageName.value != null) {
            startActivity(requireContext().packageManager.getLaunchIntentForPackage(viewModel.packageName.value!!))
        }
        findNavController().navigateUp()
    }
    
    override fun onDestroyView() {
        _fragmentLaunchAppBinding = null
        super.onDestroyView()
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }
    
}