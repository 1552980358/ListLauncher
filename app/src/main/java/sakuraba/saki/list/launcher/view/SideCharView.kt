package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import lib.github1552980358.ktExtension.android.view.heightF
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseView
import sakuraba.saki.list.launcher.view.base.TextViewInterface

class SideCharView: BaseView {
    
    companion object {
        const val LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#"
        
        interface OnLetterTouchListener {
            fun onTouch(index: Int, char: Char)
            fun onMove(index: Int, char: Char)
            fun onCancel(index: Int, char: Char)
        }
        
    }
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private var lettersDiff = 0F
    
    private var listener: OnLetterTouchListener? = null
    
    private var currentPosition = 0
    
    private val selectedPaint = Paint()
    
    init {
        (context as TextViewInterface).apply {
            if (isCustomTitleColor) {
                paint.color = customTitleColor
                selectedPaint.color = customTitleColor
            }
        }
        // Same text size
        paint.textSize = resources.getDimension(R.dimen.side_char_text_size)
        selectedPaint.textSize = resources.getDimension(R.dimen.side_char_text_size)
        // Make as bold, indicating current app shown
        selectedPaint.typeface = Typeface.create(selectedPaint.typeface, Typeface.BOLD)
    
        /**
         * For syntax '((event.y - lettersDiff) / lettersDiff)',
         * due to the drawing of 'A' (y-axis on 0) will cause the disappear of 'A'.
         * To solve this problem, just move downward to origin position of 'B'.
         * For getting relative position of all char, we need to remove a [lettersDiff]
         **/
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    when {
                        event.y <= 0 -> {
                            listener?.onTouch(0, LETTERS.first())
                        }
                        event.y >= heightF -> {
                            listener?.onTouch(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            (event.y / lettersDiff).toInt().apply {
                                listener?.onTouch(this, LETTERS[this])
                            }
                        }
                    }
                }
                ACTION_MOVE -> {
                    when {
                        event.y <= 0 -> {
                            listener?.onMove(0, LETTERS.first())
                        }
                        event.y >= heightF -> {
                            listener?.onMove(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            (event.y / lettersDiff).toInt().apply {
                                listener?.onMove(this, LETTERS[this])
                            }
                        }
                    }
                }
                ACTION_UP -> {
                    when {
                        event.y <= 0 -> {
                            listener?.onCancel(0, LETTERS.first())
                        }
                        event.y >= heightF -> {
                            listener?.onCancel(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            (event.y / lettersDiff).toInt().apply {
                                listener?.onCancel(this, LETTERS[this])
                            }
                        }
                    }
                }
            }
            
            return@setOnTouchListener true
        }
    }
    
    fun setOnLetterTouchListener(listener: OnLetterTouchListener) {
        this.listener = listener
    }
    
    fun updatePosition(char: Char) {
        currentPosition = when (char) {
            in 'A' .. 'Z' -> LETTERS.indexOf(char)
            else -> LETTERS.lastIndex
        }
        invalidate()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        @Suppress("DrawAllocation")
        lettersDiff = MeasureSpec.getSize(heightMeasureSpec).toFloat() / LETTERS.length
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        canvas?:return
        
        for ((index, char) in LETTERS.withIndex()) {
            // canvas.drawText(char.toString(), (width - paint.measureText(char.toString())) / 2, (index) * lettersDiff + lettersDiff / 2, paint)
            
            when (index) {
                currentPosition ->
                    canvas.drawText(char.toString(), (width - paint.measureText(char.toString())) / 2, (index) * lettersDiff + lettersDiff / 2, selectedPaint)
                else -> canvas.drawText(char.toString(), (width - paint.measureText(char.toString())) / 2, (index) * lettersDiff + lettersDiff / 2, paint)
            }
            
        }
        
    }
    
}