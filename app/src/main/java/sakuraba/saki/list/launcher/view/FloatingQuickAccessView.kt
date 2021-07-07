package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.widget.RelativeLayout
import android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM
import android.widget.RelativeLayout.ALIGN_PARENT_END
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setMargins
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseView

class FloatingQuickAccessView: BaseView {
    
    companion object {
        
        interface OnIconSelectedListener {
            fun onSelected(selectedNumber: Int)
            fun onMove(selectedNumber: Int)
        }
        
        private const val SELECTED_NONE = 0
        const val SELECTED_PHONE = 1
        const val SELECTED_MESSAGE = 2
        const val SELECTED_BROWSER = 3
        
    }
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private var selectedObject = -1
    private var isOnTouched = false
    
    private val normalPaint = Paint()
    private val selectedPaint = Paint()
    
    private val point1 = PointF()
    private val point2 = PointF()
    private val point3 = PointF()
    
    private val circleRadius by lazy { resources.getDimension(R.dimen.view_floating_quick_access_circle_radius) }
    private val iconSize by lazy { resources.getDimension(R.dimen.view_floating_quick_access_icon_size) }
    
    private val plusDrawable by lazy { resources.getDrawable(R.drawable.ic_plus, null) }
    private val phoneDrawable by lazy { resources.getDrawable(R.drawable.ic_phone, null) }
    private val messageDrawable by lazy { resources.getDrawable(R.drawable.ic_message, null) }
    private val browserDrawable by lazy { resources.getDrawable(R.drawable.ic_browser, null) }
    private val phoneSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_phone_selected, null) }
    private val messageSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_message_selected, null) }
    private val browserSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_browser_selected, null) }
    
    private var buttonClickColor: Int
    private var buttonNormalColor: Int
    
    private var listener: OnIconSelectedListener? = null
    
    private val clickedLayoutParam =
        RelativeLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.view_floating_quick_access_clicked_size),
            resources.getDimensionPixelSize(R.dimen.view_floating_quick_access_clicked_size)
        ).apply {
            addRule(ALIGN_PARENT_BOTTOM)
            addRule(ALIGN_PARENT_END)
            setMargins(resources.getDimensionPixelSize(R.dimen.view_floating_quick_access_margins))
        }
    
    private lateinit var normalLayoutParam: RelativeLayout.LayoutParams
    
    init {
        paint.apply {
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
        
        buttonClickColor = ContextCompat.getColor(context, R.color.purple_200)
        buttonNormalColor = ContextCompat.getColor(context, R.color.purple_500)
    
        // Initialize all configurations of normalPaint
        normalPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            color = ContextCompat.getColor(context, R.color.purple_500)
            isAntiAlias = true
        }
        
        // Initialize all configurations of selectedPaint
        selectedPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            color = ContextCompat.getColor(context, R.color.purple_500)
            isAntiAlias = true
        }
        
        point1.y = circleRadius - iconSize / 2 + normalPaint.strokeWidth
        point3.x = circleRadius - iconSize / 2 + normalPaint.strokeWidth
        
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    isOnTouched = true
                    layoutParams = clickedLayoutParam
                    selectedObject = SELECTED_NONE
                    listener?.onMove(selectedObject)
                }
                ACTION_MOVE -> {
                    selectedObject = when {
                        event.x in (point1.x .. point1.x + iconSize) && event.y in (point1.y .. point1.y + iconSize)-> SELECTED_PHONE
                        event.x in (point2.x .. point2.x + iconSize) && event.y in (point2.y .. point2.y + iconSize)-> SELECTED_MESSAGE
                        event.x in (point3.x .. point3.x + iconSize) && event.y in (point3.y .. point3.y + iconSize)-> SELECTED_BROWSER
                        else -> 0
                    }
                    listener?.onMove(selectedObject)
                }
                ACTION_UP -> {
                    isOnTouched = false
                    selectedObject = when {
                        event.x in (point1.x .. point1.x + iconSize) && event.y in (point1.y .. point1.y + iconSize)-> 1
                        event.x in (point2.x .. point2.x + iconSize) && event.y in (point2.y .. point2.y + iconSize)-> 2
                        event.x in (point3.x .. point3.x + iconSize) && event.y in (point3.y .. point3.y + iconSize)-> 3
                        else -> 0
                    }
                    listener?.onSelected(selectedObject)
                    layoutParams = normalLayoutParam
                }
            }
            invalidate()
            return@setOnTouchListener true
        }
    }
    
    fun setIconColor(newColor: Int) {
        phoneDrawable.setTint(newColor)
        messageDrawable.setTint(newColor)
        browserDrawable.setTint(newColor)
    }
    
    fun setIconSelectedColor(newColor: Int) {
        phoneSelectedDrawable.setTint(newColor)
        messageSelectedDrawable.setTint(newColor)
        browserSelectedDrawable.setTint(newColor)
    }
    
    fun setButtonIconColor(newColor: Int) {
        plusDrawable.setTint(newColor)
    }
    
    fun setButtonClickedColor(newColor: Int) {
        buttonClickColor = newColor
    }
    
    fun setButtonNormalColor(newColor: Int) {
        buttonNormalColor = newColor
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // First circle
        point1.x = MeasureSpec.getSize(widthMeasureSpec) - circleRadius - iconSize / 2 - normalPaint.strokeWidth
        
        // Second circle
        point2.x = MeasureSpec.getSize(widthMeasureSpec) / 2F - iconSize / 2
        point2.y = MeasureSpec.getSize(heightMeasureSpec) / 2F - iconSize / 2
        
        // Third circle
        point3.y = MeasureSpec.getSize(heightMeasureSpec) - circleRadius - iconSize / 2 - normalPaint.strokeWidth
    
        // Make a copy of LayoutParams
        if (!::normalLayoutParam.isInitialized) {
            normalLayoutParam = layoutParams as RelativeLayout.LayoutParams
        }
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        
        if (isOnTouched) {
    
            paint.color = ContextCompat.getColor(context, R.color.purple_200)
    
            when (selectedObject) {
                1 -> {
                    drawSelectedPhone(canvas)
                    drawNormalMessage(canvas)
                    drawNormalBrowser(canvas)
                }
                2 -> {
                    drawNormalPhone(canvas)
                    drawSelectedMessage(canvas)
                    drawNormalBrowser(canvas)
                }
                3 -> {
                    drawNormalPhone(canvas)
                    drawNormalMessage(canvas)
                    drawSelectedBrowser(canvas)
                }
                else -> {
                    drawNormalPhone(canvas)
                    drawNormalMessage(canvas)
                    drawNormalBrowser(canvas)
                }
            }
            paint.color = buttonClickColor
        } else {
            paint.color = buttonNormalColor
        }
        
        canvas.drawCircle(widthFloat - circleRadius - paint.strokeWidth, heightFloat - circleRadius - paint.strokeWidth, circleRadius, paint)
        canvas.drawBitmap(plusDrawable.toBitmap(), widthFloat - circleRadius - paint.strokeWidth - iconSize / 2, widthFloat - circleRadius - paint.strokeWidth - iconSize / 2, paint)
    }
    
    /**
     ***********************************Following are the drawing of icons***********************************
     *
     * All center axis of circles should shift to left if the circle located at right edge,
     * and vice versa, except the no edge is closed.
     * Otherwise, drawn circle will be incorrect, that part of circle disappear.
     **/
    private fun drawNormalPhone(canvas: Canvas) {
        canvas.drawCircle(point1.x + iconSize / 2, point1.y + iconSize / 2, circleRadius, normalPaint)
        canvas.drawBitmap(phoneDrawable.toBitmap(), point1.x, point1.y, normalPaint)
    }
    
    private fun drawSelectedPhone(canvas: Canvas) {
        canvas.drawCircle(point1.x + iconSize / 2, point1.y + iconSize / 2, circleRadius, selectedPaint)
        canvas.drawBitmap(phoneSelectedDrawable.toBitmap(), point1.x, point1.y, selectedPaint)
    }
    
    private fun drawNormalMessage(canvas: Canvas) {
        canvas.drawCircle(point2.x + iconSize / 2, point2.y + iconSize / 2, circleRadius, normalPaint)
        canvas.drawBitmap(messageDrawable.toBitmap(), point2.x, point2.y, normalPaint)
    }
    
    private fun drawSelectedMessage(canvas: Canvas) {
        canvas.drawCircle(point2.x + iconSize / 2, point2.y + iconSize / 2, circleRadius, selectedPaint)
        canvas.drawBitmap(messageSelectedDrawable.toBitmap(), point2.x, point2.y, selectedPaint)
    }
    
    private fun drawNormalBrowser(canvas: Canvas) {
        canvas.drawCircle(point3.x + iconSize / 2, point3.y + iconSize / 2, circleRadius, normalPaint)
        canvas.drawBitmap(browserDrawable.toBitmap(), point3.x, point3.y, normalPaint)
    }
    
    private fun drawSelectedBrowser(canvas: Canvas) {
        canvas.drawCircle(point3.x + iconSize / 2, point3.y + iconSize / 2, circleRadius, selectedPaint)
        canvas.drawBitmap(browserSelectedDrawable.toBitmap(), point3.x, point3.y, selectedPaint)
    }
    /*********************************************************************************************************/
    
}