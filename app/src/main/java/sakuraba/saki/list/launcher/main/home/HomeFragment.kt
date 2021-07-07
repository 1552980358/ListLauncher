package sakuraba.saki.list.launcher.main.home

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.promeg.pinyinhelper.Pinyin
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sakuraba.saki.list.launcher.BuildConfig
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver.Companion.APPLICATION_CHANGE_BROADCAST_RECEIVER
import sakuraba.saki.list.launcher.databinding.FragmentHomeBinding
import sakuraba.saki.list.launcher.main.setting.SettingContainer
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.util.findActivityViewById
import sakuraba.saki.list.launcher.view.FloatingQuickAccessView.Companion.OnIconSelectedListener
import sakuraba.saki.list.launcher.view.FloatingQuickAccessView.Companion.SELECTED_BROWSER
import sakuraba.saki.list.launcher.view.FloatingQuickAccessView.Companion.SELECTED_MESSAGE
import sakuraba.saki.list.launcher.view.FloatingQuickAccessView.Companion.SELECTED_NONE
import sakuraba.saki.list.launcher.view.FloatingQuickAccessView.Companion.SELECTED_PHONE
import sakuraba.saki.list.launcher.view.SideCharView.Companion.LETTERS
import sakuraba.saki.list.launcher.view.SideCharView.Companion.OnLetterTouchListener

class HomeFragment: Fragment() {
    
    private lateinit var homeViewModel: HomeViewModel
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.setSettingContainer(requireActivity().intent.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        // val root = inflater.inflate(R.layout.fragment_home, container, false)
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        fragmentHomeBinding.swipeRefreshLayout.isRefreshing = true
        return fragmentHomeBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setHasOptionsMenu(true)
        
        homeViewModel.setAppInfos(arrayListOf())
        val appInfos = homeViewModel.appInfos.value!!
        
        // val appInfos = requireContext().packageManager.queryIntent.getInstalledApplications(0)
        fragmentHomeBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentHomeBinding.recyclerView.adapter = RecyclerViewAdapter(appInfos, requireActivity())
        updateAppList()
        fragmentHomeBinding.swipeRefreshLayout.setOnRefreshListener { updateAppList() }
        (requireActivity().intent.getSerializableExtra(APPLICATION_CHANGE_BROADCAST_RECEIVER) as ApplicationChangeBroadcastReceiver).setApplicationChangeListener {
            Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.main_application_change, LENGTH_SHORT).show()
            fragmentHomeBinding.swipeRefreshLayout.isRefreshing = true
            updateAppList()
        }
        fragmentHomeBinding.sideCharView.setOnLetterTouchListener (object : OnLetterTouchListener {
            override fun onTouch(index: Int, char: Char) {
                if (!fragmentHomeBinding.swipeRefreshLayout.isRefreshing) {
                    (fragmentHomeBinding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(homeViewModel.chars[index], 0)
                    fragmentHomeBinding.textView.text = char.toString()
                    fragmentHomeBinding.textView.visibility = VISIBLE
                }
            }
            override fun onMove(index: Int, char: Char) {
                if (!fragmentHomeBinding.swipeRefreshLayout.isRefreshing) {
                    (fragmentHomeBinding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(homeViewModel.chars[index], 0)
                    fragmentHomeBinding.textView.text = char.toString()
                }
            }
            override fun onCancel(index: Int, char: Char) {
                if (!fragmentHomeBinding.swipeRefreshLayout.isRefreshing) {
                    (fragmentHomeBinding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(homeViewModel.chars[index], 0)
                    fragmentHomeBinding.textView.text = char.toString()
                    fragmentHomeBinding.textView.visibility = GONE
                }
            }
        })
        fragmentHomeBinding.floatingQuickAccessView.setOnIconSelectedListener(object : OnIconSelectedListener {
            override fun onSelected(selectedNumber: Int) {
                fragmentHomeBinding.textViewSelectedIcon.visibility = GONE
                when (selectedNumber) {
                    SELECTED_PHONE -> {
                        Intent(Intent.ACTION_DIAL).apply {
                            requireContext()
                                .packageManager
                                .resolveActivity(this, PackageManager.MATCH_DEFAULT_ONLY)
                                ?.let { startActivity(this) }
                        }
                    }
                    SELECTED_MESSAGE -> {
                        /**
                         * Following Code does not work at all.
                         * [Telephony.Sms.getDefaultSmsPackage] is workable
                         *
                         * requireContext()
                         *     .packageManager
                         *     .resolveActivity(Intent(Intent.ACTION_SEND, Uri.parse("smsto:")), PackageManager.MATCH_DEFAULT_ONLY)
                         *     ?.activityInfo
                         *     ?.packageName?.apply {
                         *         startActivity(requireContext().packageManager.getLaunchIntentForPackage(this))
                         *     }
                         **/
                        Telephony.Sms.getDefaultSmsPackage(requireContext())?.apply {
                            startActivity(requireContext().packageManager.getLaunchIntentForPackage(this))
                        }
                    }
                    SELECTED_BROWSER -> {
                        requireContext()
                            .packageManager
                            .resolveActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://example.com")
                                ), PackageManager.MATCH_DEFAULT_ONLY
                            )?.activityInfo?.packageName?.apply {
                                startActivity(requireContext().packageManager.getLaunchIntentForPackage(this))
                            }
                    }
                }
            }
    
            override fun onMove(selectedNumber: Int) {
                when (selectedNumber) {
                    SELECTED_NONE -> fragmentHomeBinding.textViewSelectedIcon.visibility = GONE
                    SELECTED_PHONE -> {
                        fragmentHomeBinding.textViewSelectedIcon.apply {
                            setText(R.string.main_text_view_selected_icon_phone)
                            visibility = VISIBLE
                        }
                    }
                    SELECTED_MESSAGE -> {
                        fragmentHomeBinding.textViewSelectedIcon.apply {
                            setText(R.string.main_text_view_selected_icon_message)
                            visibility = VISIBLE
                        }
                    }
                    SELECTED_BROWSER -> {
                        fragmentHomeBinding.textViewSelectedIcon.apply {
                            setText(R.string.main_text_view_selected_icon_browser)
                            visibility = VISIBLE
                        }
                    }
                }
            }
        })
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
            appInfos.forEach {
                it.pinYin = Pinyin.toPinyin(it.name, "").uppercase()
            }
            appInfos.sortBy { it.pinYin }
            
            val chars = homeViewModel.chars
    
            var indexChar: Int
            for ((index, appInfo) in appInfos.withIndex()) {
                indexChar = LETTERS.indexOf(appInfo.pinYin.first())
                if (indexChar == -1) {
                    if (chars[LETTERS.lastIndex] == -1) {
                        chars[LETTERS.lastIndex] = index
                    } else {
                        break
                    }
                } else if (chars[indexChar] == -1) {
                    chars[indexChar] = index
                }
            }
            
            for (index in 0 .. chars.lastIndex) {
                if (chars[index] == -1) {
                    if (index == 0) {
                        chars[index] = 0
                        continue
                    }
                    if (index == chars.lastIndex) {
                        var i = index
                        while (chars[i] == -1) {
                            i--
                        }
                        chars[index] = chars[i]
                        continue
                    }
                    chars[index] = chars[index - 1]
                }
            }
        
            // Call adapter for update of RecyclerView
            launch(Dispatchers.Main) {
                fragmentHomeBinding.recyclerView.adapter?.notifyDataSetChanged()
                // Dismiss and remove LoadingDialogFragment
                // homeViewModel.loadingDialogFragment.value?.dismiss()
                // homeViewModel.setLoadingDialogFragment()
                fragmentHomeBinding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                if (fragmentHomeBinding.swipeRefreshLayout.isRefreshing) {
                    return false
                }
                findNavController().navigate(R.id.nav_setting, Bundle().apply { putSerializable(SETTING_CONTAINER, homeViewModel.settingContainer.value) })
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentHomeBinding = null
        homeViewModel.setAppInfos(null)
    }
    
}