package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
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
    
    private var startX = 0F
    private var lettersDiff = 0F
    
    private var listener: OnLetterTouchListener? = null
    
    init {
        (context as TextViewInterface).apply {
            if (hasCustomTitleTextColor()) {
                paint.color = getTitleTextColor()
            }
        }
        paint.textSize = resources.getDimension(R.dimen.side_char_text_size)
    
        /**
         * For syntax '((event.y - lettersDiff) / lettersDiff)',
         * due to the drawing of 'A' on ([startX], 0) will cause the disappear of 'A'.
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
                        event.y >= heightFloat -> {
                            listener?.onTouch(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            ((event.y - lettersDiff) / lettersDiff).toInt().apply {
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
                        event.y >= heightFloat -> {
                            listener?.onMove(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            ((event.y - lettersDiff) / lettersDiff).toInt().apply {
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
                        event.y >= heightFloat -> {
                            listener?.onCancel(LETTERS.lastIndex, LETTERS.last())
                        }
                        else -> {
                            ((event.y - lettersDiff) / lettersDiff).toInt().apply {
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
    
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        @Suppress("DrawAllocation")
        Rect().apply {
            paint.getTextBounds(LETTERS.first().toString(), 0, 1, this)
            startX = (MeasureSpec.getSize(widthMeasureSpec) - width()) / 2F
        }
        lettersDiff = MeasureSpec.getSize(heightMeasureSpec).toFloat() / (LETTERS.length + 1F)
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        canvas?:return
        
        for ((index, char) in LETTERS.withIndex()) {
            canvas.drawText(char.toString(), startX, (index + 1) * lettersDiff, paint)
        }
        
    }
    
}