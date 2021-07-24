package sakuraba.saki.list.launcher.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.SeekBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import sakuraba.saki.list.launcher.databinding.FragmentSeekbarDialogBinding

class SeekbarDialogFragment(
    @StringRes private val title: Int,
    @StringRes private val positive: Int,
    @StringRes private val negative: Int,
    @StringRes private val default: Int,
    private val max: Int = 1,
    private val min: Int = 0,
    private val initValue: Int,
    private val listener: OnSelectListener): DialogFragment() {
    
    companion object {
        interface OnSelectListener {
            fun onPositive(value: Int)
            fun onNegative()
            fun onDefault()
        }
        private const val TAG = "SeekbarDialogFragment"
    }
    
    private var _fragmentSeekbarDialogBinding: FragmentSeekbarDialogBinding? = null
    private val fragmentSeekbarDialogBinding get() = _fragmentSeekbarDialogBinding!!
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _fragmentSeekbarDialogBinding = FragmentSeekbarDialogBinding.inflate(layoutInflater)
        fragmentSeekbarDialogBinding.seekbar.max = this@SeekbarDialogFragment.max - this@SeekbarDialogFragment.min
        fragmentSeekbarDialogBinding.textViewMax.text = max.toString()
        fragmentSeekbarDialogBinding.textViewMin.text = min.toString()
        fragmentSeekbarDialogBinding.textViewCur.text = initValue.toString()
        fragmentSeekbarDialogBinding.seekbar.progress = initValue - min
        fragmentSeekbarDialogBinding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, isUser: Boolean) {
                fragmentSeekbarDialogBinding.textViewCur.text = (progress + min).toString()
            }
            override fun onStartTrackingTouch(seekbar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekbar: SeekBar?) = Unit
        })
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(fragmentSeekbarDialogBinding.root)
            .setPositiveButton(positive) { _, _ -> listener.onPositive(fragmentSeekbarDialogBinding.seekbar.progress + min) }
            .setNegativeButton(negative) { _, _ -> listener.onNegative() }
            .setNeutralButton(default) { _, _ -> listener.onDefault() }
            .create()
    }
    
    fun show(manager: FragmentManager) {
        show(manager, TAG)
    }
    
}