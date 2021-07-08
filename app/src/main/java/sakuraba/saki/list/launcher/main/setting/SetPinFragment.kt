package sakuraba.saki.list.launcher.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentSetPinBinding
import sakuraba.saki.list.launcher.main.setting.SecuritySettingFragment.Companion.LAUNCH_TASK
import sakuraba.saki.list.launcher.main.setting.SecuritySettingFragment.Companion.LAUNCH_TASK_MODIFY
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_PIN_CODE
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_FINGERPRINT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_USE_PIN
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER
import sakuraba.saki.list.launcher.viewGroup.PinInputLayout.Companion.KEY_BACKSPACE
import sakuraba.saki.list.launcher.viewGroup.PinInputLayout.Companion.KEY_CUSTOM

class SetPinFragment: Fragment() {
    
    private var _fragmentSetPinBinding: FragmentSetPinBinding? = null
    private val fragmentSetPinBinding get() = _fragmentSetPinBinding!!
    
    private var _inputFirst = ""
    private var _inputSecond = ""
    private var _moveToSecond = false
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentSetPinBinding = FragmentSetPinBinding.inflate(inflater)
        return fragmentSetPinBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSetPinBinding.pinInputLayout.setOnButtonClickListener { button, buttonChar ->
            newInput(button, buttonChar)
        }
    }
    
    private fun newInput(button: Int, char: Char) {
        when (button) {
            KEY_BACKSPACE -> {
                if (_inputFirst.isEmpty()) {
                    return
                }
                if (_inputFirst.length < 4) {
                    _inputFirst = _inputFirst.substring(0, _inputFirst.length - 1)
                    fragmentSetPinBinding.pinInputLayout.removeKey()
                    return
                }
                if (_inputSecond.isEmpty()) {
                    return
                }
                if (_inputSecond.length < 4) {
                    _inputFirst = _inputFirst.substring(0, _inputFirst.length - 1)
                    fragmentSetPinBinding.pinInputLayout.removeKey()
                }
            }
            KEY_CUSTOM -> { }
            else ->{
                if (!_moveToSecond) {
                    if (_inputFirst.length < 4) {
                        _inputFirst += char
                        fragmentSetPinBinding.pinInputLayout.updateKey(_inputFirst.length)
                    }
                    if (_inputFirst.length == 4) {
                        fragmentSetPinBinding.textView.setText(R.string.set_pin_title_2)
                        fragmentSetPinBinding.pinInputLayout.updateKey(0)
                        _moveToSecond = !_moveToSecond
                        return
                    }
                    return
                }
                if (_inputSecond.length != 4) {
                    _inputSecond += char
                }
                if (_inputSecond.length == 4) {
                    findNavController().navigateUp()
                }
            }
        }
    }
    
    private fun checkInputs() {
        if (_inputFirst.length != 4 || _inputSecond.length != 4 || _inputFirst != _inputSecond) {
            if (arguments?.getInt(LAUNCH_TASK) != LAUNCH_TASK_MODIFY) {
                @Suppress("ApplySharedPref")
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .edit()
                    .putBoolean(KEY_USE_FINGERPRINT, false)
                    .putBoolean(KEY_USE_PIN, false)
                    .commit()
                (arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer?)?.apply {
                    getBooleanUpdate(KEY_USE_FINGERPRINT, false)
                    getBooleanUpdate(KEY_USE_PIN, false)
                }
            }
            return
        }
        @Suppress("ApplySharedPref")
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .edit()
            .putString(KEY_PIN_CODE, _inputSecond)
            .commit()
        (arguments?.getSerializable(SETTING_CONTAINER) as SettingContainer?)?.apply {
            getStringUpdate(KEY_PIN_CODE, _inputFirst)
            getBooleanUpdate(KEY_USE_PIN, true)
        }
    }
    
    override fun onDestroyView() {
        _fragmentSetPinBinding = null
        checkInputs()
        super.onDestroyView()
    }
    
}