package sakuraba.saki.list.launcher.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentSearchBinding
import sakuraba.saki.list.launcher.main.home.AppInfo

class SearchFragment: Fragment(), KeyboardUtil {
    
    companion object {
        private const val SPLIT_SPACE = " "
        const val APP_INFOS = "APP_INFOS"
    }
    
    private var _fragmentSearchBinding: FragmentSearchBinding? = null
    private val fragmentSearchBinding get() = _fragmentSearchBinding!!
    
    private var job: Job? = null
    
    private lateinit var viewModel: SearchViewModel
    
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        @Suppress("UNCHECKED_CAST")
        viewModel.setAppInfos(arguments?.getSerializable(APP_INFOS) as ArrayList<AppInfo>?)
        _fragmentSearchBinding = FragmentSearchBinding.inflate(inflater)
        return fragmentSearchBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSearchBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentSearchBinding.recyclerView.adapter = RecyclerViewAdapter(viewModel.searchResult, fragmentSearchBinding.textView, requireActivity())
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        (menu.findItem(R.id.menu_search_view).actionView as SearchView).apply {
            isIconified = false
            onActionViewExpanded()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true
                override fun onQueryTextChange(newText: String?): Boolean {
                    val searchResult = viewModel.searchResult
                    searchResult.clear()
                    
                    when {
                        newText.isNullOrEmpty() ->{
                            fragmentSearchBinding.textView.setText(R.string.search_hint)
                        }
                        else -> {
                            newText.split(SPLIT_SPACE).toMutableList().apply {
                                removeAll { it.isEmpty() }
                                when {
                                    isEmpty() -> fragmentSearchBinding.textView.setText(R.string.search_empty_filter_result)
                                    else -> {
                                        val appInfos = viewModel.appInfos
                                        forEach { text ->
                                            appInfos.forEach { appInfo ->
                                                if (appInfo.name.contains(text) || appInfo.packageName.contains(text) || appInfo.pinYin.contains(text)) {
                                                    if (!searchResult.contains(appInfo)) {
                                                        searchResult.add(appInfo)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    fragmentSearchBinding.recyclerView.adapter?.notifyDataSetChanged()
                    return true
                }
            })
        }
    }
    
    override fun onDestroyView() {
        _fragmentSearchBinding = null
        hideKeyboard(requireActivity())
        super.onDestroyView()
    }

}