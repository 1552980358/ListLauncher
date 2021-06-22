package sakuraba.saki.list.launcher.main.setting

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentColorPickDialogBinding
import java.io.Serializable

class ColorPickDialogFragment(private val listener: OnColorPickListener?): DialogFragment() {
    
    companion object {
        interface OnColorPickListener: Serializable {
            fun onColorPick(@ColorInt color: Int)
            fun onSelectDefault()
            fun onCancel()
        }
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
        }
        fragmentColorPickDialogBinding.colorTransparencyView.setOnColorTransparencyChangeListener { color ->
            fragmentColorPickDialogBinding.colorPresentViewNew.updateColor(color)
            fragmentColorPickDialogBinding.editTextRGB.tag = color
            fragmentColorPickDialogBinding.editTextRGB.setText(String.format("#%08X", color))
        }
        fragmentColorPickDialogBinding.editTextRGB.addTextChangedListener {
            if (fragmentColorPickDialogBinding.editTextRGB.tag == null) {
                if (it?.length == 9) {
                    fragmentColorPickDialogBinding.colorPlateView.setHSV(it.toString())
                }
            } else {
                fragmentColorPickDialogBinding.editTextRGB.tag = null
            }
        }
        fragmentColorPickDialogBinding.colorPlateView.setHSV("#FFFF0000")
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.color_pick_title)
            .setView(fragmentColorPickDialogBinding.root)
            .setPositiveButton(R.string.color_pick_confirm) { _, _ ->
                listener?.onColorPick(fragmentColorPickDialogBinding.colorPresentViewNew.color)
            }
            .setNegativeButton(R.string.color_pick_cancel) { _, _ ->
                listener?.onCancel()
            }
            .setNeutralButton(R.string.color_pick_default) { _, _ ->
                listener?.onSelectDefault()
            }
            .create()
    }
    
}