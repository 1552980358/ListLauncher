package sakuraba.saki.list.launcher.main.launchApp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentPinInputDialogBinding
import sakuraba.saki.list.launcher.main.launchApp.PinInputViewModel.Companion.CLEAR_ALL
import sakuraba.saki.list.launcher.main.setting.SettingContainer
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.viewGroup.PinInputLayout.Companion.KEY_CUSTOM

class PinInputDialogFragment(private val authorizationListener: AuthorizationListener?): BottomSheetDialogFragment() {
    
    companion object {
        const val PIN_MAX_SIZE = 4
        const val TAG = "PinInputDialogFragment"
    }
    
    private var _fragmentPinInputDialogBinding: FragmentPinInputDialogBinding? = null
    private val fragmentPinInputDialogBinding get() = _fragmentPinInputDialogBinding!!
    private lateinit var viewModel: PinInputViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentBackgroundBottomSheetDialogTheme)
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _fragmentPinInputDialogBinding = FragmentPinInputDialogBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(PinInputViewModel::class.java)
        viewModel.setSettingContainer(requireActivity().intent?.getSerializableExtra(SETTING_CONTAINER) as SettingContainer?)
        var pinTry = 0
        viewModel.pinCode.observe(this) { pinCode ->
            if (pinCode.isEmpty()) {
                return@observe
            }
            if (pinCode.length == PIN_MAX_SIZE) {
                if (pinTry == 3) {
                    dismiss()
                    authorizationListener?.onAuthFailed()
                }
                if (viewModel.settingContainer.value?.getStringValue(KEY_PIN_CODE) == pinCode) {
                    dismiss()
                    authorizationListener?.onAuthComplete()
                    return@observe
                }
                viewModel.newKeyInput(CLEAR_ALL)
                fragmentPinInputDialogBinding.pinInputLayout.removeAllKey()
                pinTry++
            }
        }
        fragmentPinInputDialogBinding.pinInputLayout.setOnButtonClickListener { button: Int, buttonChar: Char ->
            if (button != KEY_CUSTOM) {
                viewModel.newKeyInput(buttonChar)
            }
        }
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(fragmentPinInputDialogBinding.root)
        }
    }
    
    override fun onDestroyView() {
        _fragmentPinInputDialogBinding = null
        super.onDestroyView()
    }
    
    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
    
}