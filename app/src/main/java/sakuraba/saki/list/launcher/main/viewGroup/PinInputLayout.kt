package sakuraba.saki.list.launcher.main.viewGroup

import android.app.Service
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import sakuraba.saki.list.launcher.databinding.LayoutPinInputBinding

class PinInputLayout: LinearLayout {
    
    companion object {
        
        fun interface OnButtonClickListener {
            fun onClick(button: Int, buttonChar: Char): Boolean
        }
        
        const val KEY_1 = 1
        const val KEY_2 = 2
        const val KEY_3 = 3
        const val KEY_4 = 4
        const val KEY_5 = 5
        const val KEY_6 = 6
        const val KEY_7 = 7
        const val KEY_8 = 8
        const val KEY_9 = 9
        const val KEY_0 = 0
        const val KEY_BACKSPACE = 10
        const val KEY_CUSTOM = 11
        
        private const val KEY_0_CHAR = '0'
        private const val KEY_1_CHAR = '1'
        private const val KEY_2_CHAR = '2'
        private const val KEY_3_CHAR = '3'
        private const val KEY_4_CHAR = '4'
        private const val KEY_5_CHAR = '5'
        private const val KEY_6_CHAR = '6'
        private const val KEY_7_CHAR = '7'
        private const val KEY_8_CHAR = '8'
        private const val KEY_9_CHAR = '9'
        private const val KEY_BACKSPACE_CHAR = ' '
        private const val KEY_CUSTOM_CHAR = KEY_CUSTOM.toChar()
    }
    
    private var onButtonClickListener: OnButtonClickListener? = null
    
    private var _layoutPinInputBinding: LayoutPinInputBinding? = null
    private val layoutPinInputBinding get() = _layoutPinInputBinding!!
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private var _numberClick = 0
    
    init {
        _layoutPinInputBinding =
            LayoutPinInputBinding.inflate(context.getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        addView(layoutPinInputBinding.root)
        layoutPinInputBinding.apply {
            textViewNum0.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_0, KEY_0_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum1.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_1, KEY_1_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum2.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_2, KEY_2_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum3.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_3, KEY_3_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum4.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_4, KEY_4_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum5.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_5, KEY_5_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum6.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_6, KEY_6_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum7.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_7, KEY_7_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum8.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_8, KEY_8_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewNum9.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_9, KEY_9_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
            textViewBackSpace.setOnClickListener {
                if (onButtonClickListener?.onClick(KEY_BACKSPACE, KEY_BACKSPACE_CHAR) == false) {
                    return@setOnClickListener
                }
                removeKey()
            }
            textView.setOnClickListener { 
                if (onButtonClickListener?.onClick(KEY_CUSTOM, KEY_CUSTOM_CHAR) == false) {
                    return@setOnClickListener
                }
                newKey()
            }
        }
    }
    
    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener
    }
    
    fun removeAllKey() = removeKey(layoutPinInputBinding.pinCodeView.getMaxInputSize())
    
    fun newKey() = newKey(1)
    
    fun newKey(newKey: Int) {
        _numberClick += if (newKey > layoutPinInputBinding.pinCodeView.getMaxInputSize()) {
                layoutPinInputBinding.pinCodeView.getMaxInputSize() - _numberClick
            } else { newKey }
        layoutPinInputBinding.pinCodeView.update(_numberClick)
    }
    
    fun removeKey() = removeKey(1)
    
    fun removeKey(key: Int) {
        _numberClick -= if (key > _numberClick) { _numberClick } else { key }
        layoutPinInputBinding.pinCodeView.update(_numberClick)
    }
    
    fun setMaxInput(newSize: Int) = layoutPinInputBinding.pinCodeView.setMaxInputSize(newSize)
    
    fun setRadius(newRadius: Float) = layoutPinInputBinding.pinCodeView.updateRadius(newRadius)
    
    fun setCircleMargin(newMargin: Float) = layoutPinInputBinding.pinCodeView.updateMargin(newMargin)
    
}