package sakuraba.saki.list.launcher.main.home

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sakuraba.saki.list.launcher.BuildConfig
import sakuraba.saki.list.launcher.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    
    private lateinit var homeViewModel: HomeViewModel
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // val root = inflater.inflate(R.layout.fragment_home, container, false)
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        fragmentHomeBinding.root.isRefreshing = true
        return fragmentHomeBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        homeViewModel.setAppInfos(arrayListOf())
        val appInfos = homeViewModel.appInfos.value!!
        
        // val appInfos = requireContext().packageManager.queryIntent.getInstalledApplications(0)
        fragmentHomeBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentHomeBinding.recyclerView.adapter = RecyclerViewAdapter(appInfos, requireActivity())
        updateAppList()
        fragmentHomeBinding.root.setOnRefreshListener { updateAppList() }
    }
    
    private fun updateAppList() {
        val appInfos = homeViewModel.appInfos.value!!
        appInfos.clear()
        // homeViewModel.setLoadingDialogFragment(LoadingDialogFragment())
        // homeViewModel.loadingDialogFragment.value?.show(requireActivity().supportFragmentManager)
        @Suppress("EXPERIMENTAL_API_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            // Query out all application packages that is launchable by a launcher
            requireContext().packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }, 0).forEach { resolveInfo ->
            
                // Ignore this launcher by checking package name
                if (resolveInfo.resolvePackageName != BuildConfig.APPLICATION_ID) {
                    requireActivity().packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0).apply {
                        @Suppress("DEPRECATION")
                        appInfos.add(
                            AppInfo(
                                resolveInfo.loadLabel(requireContext().packageManager).toString(),
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.loadIcon(requireContext().packageManager),
                                requireActivity().packageManager.getApplicationInfo(
                                    resolveInfo.activityInfo.packageName,
                                    0
                                ).flags and ApplicationInfo.FLAG_SYSTEM != 0,
                                versionName,
                                if (Build.VERSION.SDK_INT >= 28) longVersionCode else versionCode.toLong()
                            )
                        )
                    }
                }
            }
        
            // Short by converting all Chinese into characters for comparing
            // Will further support more language
            appInfos.sortBy { Pinyin.toPinyin(it.name, "") }
        
            // Call adapter for update of RecyclerView
            launch(Dispatchers.Main) {
                fragmentHomeBinding.recyclerView.adapter?.notifyDataSetChanged()
                // Dismiss and remove LoadingDialogFragment
                // homeViewModel.loadingDialogFragment.value?.dismiss()
                // homeViewModel.setLoadingDialogFragment()
                fragmentHomeBinding.root.isRefreshing = false
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentHomeBinding = null
        homeViewModel.setAppInfos(null)
    }
    
}