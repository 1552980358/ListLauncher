package sakuraba.saki.list.launcher.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentApplicationInfoBottomDialogBinding

class ApplicationInfoBottomSheetDialogFragment(private val appInfo: AppInfo): BottomSheetDialogFragment() {
    
    companion object {
        private const val TAG = "ApplicationInfoBottomSheetDialog"
    }
    
    private var _fragmentApplicationInfoBottomDialog: FragmentApplicationInfoBottomDialogBinding? = null
    private val fragmentApplicationInfoBottomDialogBinding get() = _fragmentApplicationInfoBottomDialog!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBackgroundBottomSheetDialogTheme)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentApplicationInfoBottomDialog = FragmentApplicationInfoBottomDialogBinding.inflate(inflater)
        fragmentApplicationInfoBottomDialogBinding.imageViewIcon.setImageDrawable(appInfo.icon)
        fragmentApplicationInfoBottomDialogBinding.textViewApplicationName.text = appInfo.name
        fragmentApplicationInfoBottomDialogBinding.textViewPackageName.text = appInfo.packageName
        fragmentApplicationInfoBottomDialogBinding.textViewSystemApp.apply {
            if (appInfo.isSystem) {
                setText(R.string.bottom_dialog_home_system_yes)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                setText(R.string.bottom_dialog_home_system_no)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
        fragmentApplicationInfoBottomDialogBinding.textViewVersionName.text = appInfo.versionName
        fragmentApplicationInfoBottomDialogBinding.textViewVersionCode.text = appInfo.versionCode.toString()
        return fragmentApplicationInfoBottomDialogBinding.root
    }
    
    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }
    
    override fun onDestroyView() {
        _fragmentApplicationInfoBottomDialog = null
        super.onDestroyView()
    }
    
}