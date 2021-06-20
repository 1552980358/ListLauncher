package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sakuraba.saki.list.launcher.databinding.FragmentPinAuthorizeBinding
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener.Companion.AUTHORIZATION_LISTENER
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.viewGroup.PinInputLayout.Companion.KEY_BACKSPACE
import sakuraba.saki.list.launcher.viewGroup.PinInputLayout.Companion.KEY_CUSTOM

class PinAuthorizeFragment: Fragment() {
    
    private var _fragmentPinAuthorizeBinding: FragmentPinAuthorizeBinding? = null
    private val fragmentPinAuthorizeBinding get() = _fragmentPinAuthorizeBinding!!
    private lateinit var viewModel: PinAuthorizeViewModel
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentPinAuthorizeBinding = FragmentPinAuthorizeBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(PinAuthorizeViewModel::class.java)
        viewModel.setSettingContainer(arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer?)
        viewModel.setAuthorizationListener(arguments?.getSerializable(AUTHORIZATION_LISTENER) as AuthorizationListener?)
        return fragmentPinAuthorizeBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pin.observe(this) { pin ->
            fragmentPinAuthorizeBinding.pinInputLayout.updateKey(pin.length)
            if (pin.length == 4) {
                findNavController().navigateUp()
            }
        }
        fragmentPinAuthorizeBinding.pinInputLayout.setOnButtonClickListener { button, buttonChar ->
            when (button) {
                KEY_BACKSPACE -> {
                    viewModel.pinRemove()
                    return@setOnButtonClickListener
                }
                KEY_CUSTOM -> { return@setOnButtonClickListener }
                else -> {
                    if (viewModel.pin.value!!.length != 4) {
                        viewModel.pinAdd(buttonChar)
                    }
                }
            }
        }
        
    }
    
    override fun onDestroyView() {
        if (viewModel.pinLength == 4) {
            viewModel.authorizationListener.value?.onAuthComplete()
        } else {
            viewModel.authorizationListener.value?.onAuthFailed()
        }
        _fragmentPinAuthorizeBinding = null
        super.onDestroyView()
    }
    
}