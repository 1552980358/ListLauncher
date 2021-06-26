package sakuraba.saki.list.launcher.main.setting

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentColorPickDialogBinding
import sakuraba.saki.list.launcher.util.hexStrToInt
import java.io.Serializable

class ColorPickDialogFragment(private val listener: OnColorPickListener?): DialogFragment() {
    
    companion object {
        interface OnColorPickListener: Serializable {
            fun onColorPick(@ColorInt color: Int, colorStr: String)
            fun onSelectDefault()
            fun onCancel()
        }
        
        private const val TAG = "ColorPickDialogFragment"
    }
    
    private var _fragmentColorPickDialogBinding: FragmentColorPickDialogBinding? = null
    private val fragmentColorPickDialogBinding get() = _fragmentColorPickDialogBinding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _fragmentColorPickDialogBinding = FragmentColorPickDialogBinding.inflate(layoutInflater)
        fragmentColorPickDialogBinding.colorPadView.setOnHSEChangeListener { hse ->
            fragmentColorPickDialogBinding.colorPlateView.setHSE(hse)
        }
        fragmentColorPickDialogBinding.colorPlateView.setOnColorChangeListener { color ->
            fragmentColorPickDialogBinding.colorTransparencyView.updateColor(color)
            fragmentColorPickDialogBinding.editTextRGB.tag = color
            @Suppress("SetTextI18n")
            fragmentColorPickDialogBinding.editTextRGB.setText("#${String.format("%08X", color).substring(2, 8)}")
        }
        fragmentColorPickDialogBinding.colorTransparencyView.setOnColorTransparencyChangeListener { color, alpha ->
            fragmentColorPickDialogBinding.colorPresentViewNew.updateColor(color)
            fragmentColorPickDialogBinding.editTextAlpha.tag = alpha
            fragmentColorPickDialogBinding.editTextAlpha.setText(String.format("%02X", alpha))
        }
        fragmentColorPickDialogBinding.editTextRGB.addTextChangedListener {
            if (fragmentColorPickDialogBinding.editTextRGB.tag == null) {
                if (it?.length == 7) {
                    fragmentColorPickDialogBinding.colorPlateView.setHSV(it.toString())
                }
            } else {
                fragmentColorPickDialogBinding.editTextRGB.tag = null
            }
        }
        fragmentColorPickDialogBinding.editTextAlpha.addTextChangedListener {
            if (fragmentColorPickDialogBinding.editTextAlpha.tag == null) {
                if (it?.length == 2) {
                    fragmentColorPickDialogBinding.colorTransparencyView.updateAlpha(it.toString().hexStrToInt)
                }
            } else {
                fragmentColorPickDialogBinding.editTextAlpha.tag = null
            }
        }
        fragmentColorPickDialogBinding.colorPlateView.setHSV("#FFFF0000")
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.color_pick_title)
            .setView(fragmentColorPickDialogBinding.root)
            .setPositiveButton(R.string.color_pick_confirm) { _, _ ->
                listener?.onColorPick(fragmentColorPickDialogBinding.colorPresentViewNew.color, String.format("%08X", fragmentColorPickDialogBinding.colorPresentViewNew.color))
            }
            .setNegativeButton(R.string.color_pick_cancel) { _, _ ->
                listener?.onCancel()
            }
            .setNeutralButton(R.string.color_pick_default) { _, _ ->
                listener?.onSelectDefault()
            }
            .create()
    }
    
    fun show(manager: FragmentManager) = show(manager, TAG)
    
}