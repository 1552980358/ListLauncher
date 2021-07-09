package sakuraba.saki.list.launcher.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentApplicationInfoDialogBinding

class ApplicationInfoDialogFragment(private val appInfo: AppInfo): BottomSheetDialogFragment() {
    
    companion object {
        private const val TAG = "ApplicationInfoBottomSheetDialog"
    }
    
    private var _fragmentApplicationInfoDialogBinding: FragmentApplicationInfoDialogBinding? = null
    private val fragmentApplicationInfoDialogBinding get() = _fragmentApplicationInfoDialogBinding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBackgroundBottomSheetDialogTheme)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentApplicationInfoDialogBinding = FragmentApplicationInfoDialogBinding.inflate(inflater)
        fragmentApplicationInfoDialogBinding.imageViewIcon.setImageDrawable(appInfo.icon)
        fragmentApplicationInfoDialogBinding.textViewApplicationName.text = appInfo.name
        fragmentApplicationInfoDialogBinding.textViewPackageName.text = appInfo.packageName
        fragmentApplicationInfoDialogBinding.textViewSystemApp.apply {
            if (appInfo.isSystem) {
                setText(R.string.bottom_dialog_home_system_yes)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            } else {
                setText(R.string.bottom_dialog_home_system_no)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            }
        }
        fragmentApplicationInfoDialogBinding.textViewVersionName.text = appInfo.versionName
        fragmentApplicationInfoDialogBinding.textViewVersionCode.text = appInfo.versionCode.toString()
        fragmentApplicationInfoDialogBinding.textViewSetting.setOnClickListener {
            dismiss()
            requireContext().startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", appInfo.packageName, null)))
        }
        
        fragmentApplicationInfoDialogBinding.textViewCancel.setOnClickListener { dismiss() }
        return fragmentApplicationInfoDialogBinding.root
    }
    
    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }
    
    override fun onDestroyView() {
        _fragmentApplicationInfoDialogBinding = null
        super.onDestroyView()
    }
    
}