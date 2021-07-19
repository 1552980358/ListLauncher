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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lib.github1552980358.ktExtension.android.view.heightF
import lib.github1552980358.ktExtension.android.view.widthF
import lib.github1552980358.ktExtension.jvm.keyword.tryOnly
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseView

class FloatingQuickAccessView: BaseView {
    
    companion object {
        
        interface OnIconSelectedListener {
            fun onSelected(selectedNumber: Int)
            fun onMove(selectedNumber: Int)
        }
        
        const val SELECTED_NONE = 0
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
    private val plusTouchedDrawable by lazy { resources.getDrawable(R.drawable.ic_plus, null) }
    private val phoneDrawable by lazy { resources.getDrawable(R.drawable.ic_phone, null) }
    private val messageDrawable by lazy { resources.getDrawable(R.drawable.ic_message, null) }
    private val browserDrawable by lazy { resources.getDrawable(R.drawable.ic_browser, null) }
    private val phoneSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_phone_selected, null) }
    private val messageSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_message_selected, null) }
    private val browserSelectedDrawable by lazy { resources.getDrawable(R.drawable.ic_browser_selected, null) }
    
    // private var buttonNormalColor: Int
    // private var buttonClickColor: Int
    private var buttonBackgroundColorNormal: Int? = null
    private var buttonBackgroundStrokeColorNormal: Int? = null
    private var buttonBackgroundColorTouched: Int? = null
    private var buttonBackgroundStrokeColorTouched: Int? = null
    
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
    
    private var animatingJob: Job? = null
    private var isAnimating = false
    private var animationPos = 0
    
    private var iconBackgroundColor: Int? = null
    private var iconBackgroundStrokeColor: Int? = null
    private var iconBackgroundSelectedColor: Int? = null
    private var iconBackgroundSelectedStrokeColor: Int? = null
    
    init {
        paint.apply {
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
        
        // buttonClickColor = ContextCompat.getColor(context, R.color.purple_200)
        // buttonNormalColor = ContextCompat.getColor(context, R.color.purple_500)
    
        // Initialize all configurations of normalPaint
        normalPaint.apply {
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            isAntiAlias = true
        }
        
        // Initialize all configurations of selectedPaint
        selectedPaint.apply {
            strokeWidth = resources.getDimension(R.dimen.view_floating_quick_access_circle_stroke)
            isAntiAlias = true
        }
        
        point1.y = circleRadius - iconSize / 2 + normalPaint.strokeWidth
        point3.x = circleRadius - iconSize / 2 + normalPaint.strokeWidth
        
        iconBackgroundColor = ContextCompat.getColor(context, R.color.purple_500)
        iconBackgroundStrokeColor = ContextCompat.getColor(context, R.color.purple_500)
        iconBackgroundSelectedColor = ContextCompat.getColor(context, R.color.white)
        iconBackgroundSelectedStrokeColor = ContextCompat.getColor(context, R.color.purple_500)
    
        buttonBackgroundColorNormal = ContextCompat.getColor(context, R.color.purple_500)
        buttonBackgroundStrokeColorNormal = ContextCompat.getColor(context, R.color.purple_500)
        buttonBackgroundColorTouched = ContextCompat.getColor(context, R.color.purple_200)
        buttonBackgroundStrokeColorTouched = ContextCompat.getColor(context, R.color.purple_200)
        
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    // Force stop any executing animation
                    isAnimating = false
                    animatingJob?.cancel()
                    
                    isOnTouched = true
                    layoutParams = clickedLayoutParam
                    selectedObject = SELECTED_NONE
                    listener?.onMove(selectedObject)
                    
                    animatingJob = CoroutineScope(Dispatchers.IO).launch {
                        isAnimating = true
                        // Start pos
                        animationPos = 0
                        repeat(9) {
                            if (!isAnimating) {
                                return@launch
                            }
                            delay(10)
                            animationPos++
                            postInvalidate()
                        }
                        isAnimating = false
                    }
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
                    // Force stop any executing animation
                    isAnimating = false
                    animatingJob?.cancel()
                    
                    isOnTouched = false
                    selectedObject = when {
                        event.x in (point1.x .. point1.x + iconSize) && event.y in (point1.y .. point1.y + iconSize)-> SELECTED_PHONE
                        event.x in (point2.x .. point2.x + iconSize) && event.y in (point2.y .. point2.y + iconSize)-> SELECTED_MESSAGE
                        event.x in (point3.x .. point3.x + iconSize) && event.y in (point3.y .. point3.y + iconSize)-> SELECTED_BROWSER
                        else -> 0
                    }
                    listener?.onSelected(selectedObject)
                    
                    animatingJob = CoroutineScope(Dispatchers.IO).launch {
                        isAnimating = true
                        // Start pos
                        animationPos = 9
                        repeat(9) {
                            if (!isAnimating) {
                                return@launch
                            }
                            delay(10)
                            animationPos--
                            postInvalidate()
                        }
                        isAnimating = false
                        launch(Dispatchers.Main) {
                            layoutParams = normalLayoutParam
                        }
                    }
                }
            }
            invalidate()
            return@setOnTouchListener true
        }
    }
    
    fun setOnIconSelectedListener(listener: OnIconSelectedListener?) {
        this.listener = listener
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
    
    fun setIconBackgroundColor(newColor: Int?) {
        iconBackgroundColor = newColor
    }
    
    fun setIconBackgroundStrokeColor(newColor: Int?) {
        iconBackgroundStrokeColor = newColor
    }
    
    fun setIconBackgroundSelectedColor(newColor: Int?) {
        iconBackgroundSelectedColor = newColor
    }
    
    fun setIconBackgroundSelectedStrokeColor(newColor: Int?) {
        iconBackgroundSelectedStrokeColor = newColor
    }
    
    fun setButtonNormalColor(newColor: Int) {
        // buttonNormalColor = newColor
        plusDrawable.setTint(newColor)
    }
    
    fun setButtonClickedColor(newColor: Int) {
        // buttonClickColor = newColor
        plusTouchedDrawable.setTint(newColor)
    }
    
    fun setButtonBackgroundColorNormal(newColor: Int?) {
        buttonBackgroundColorNormal = newColor
    }
    fun setButtonBackgroundStrokeColorNormal(newColor: Int?) {
        buttonBackgroundStrokeColorNormal = newColor
    }
    
    fun setButtonBackgroundColorTouched(newColor: Int?) {
        buttonBackgroundColorTouched = newColor
    }
    
    fun setButtonBackgroundStrokeColorTouched(newColor: Int?) {
        buttonBackgroundStrokeColorTouched = newColor
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
            tryOnly { normalLayoutParam = layoutParams as RelativeLayout.LayoutParams }
        }
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        
        when {
            isAnimating -> {
                drawNormalBackground(
                    canvas,
                    iconBackgroundColor,
                    point1.x + iconSize / 2,
                    (heightF - circleRadius - paint.strokeWidth) + ((point1.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                )
                drawNormalBackgroundStroke(
                    canvas,
                    iconBackgroundStrokeColor,
                    point1.x + iconSize / 2,
                    (heightF - circleRadius - paint.strokeWidth) + ((point1.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                )
    
                drawNormalBackground(
                    canvas,
                    iconBackgroundColor,
                    (widthF - circleRadius - paint.strokeWidth) + ((point2.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                    (heightF - circleRadius - paint.strokeWidth) + ((point2.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos
                )
                drawNormalBackgroundStroke(
                    canvas,
                    iconBackgroundStrokeColor,
                    (widthF - circleRadius - paint.strokeWidth) + ((point2.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                    (heightF - circleRadius - paint.strokeWidth) + ((point2.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos
                )
    
                drawNormalBackground(
                    canvas,
                    iconBackgroundColor,
                    (widthF - circleRadius - paint.strokeWidth) + ((point3.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                    point3.y + iconSize / 2
                )
                drawNormalBackgroundStroke(
                    canvas,
                    iconBackgroundStrokeColor,
                    (widthF - circleRadius - paint.strokeWidth) + ((point3.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                    point3.y + iconSize / 2
                )
                // canvas.drawCircle(
                //    point1.x + iconSize / 2,
                //    (heightF - circleRadius - paint.strokeWidth) + ((point1.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                //    circleRadius,
                //    normalPaint
                // )
                // canvas.drawCircle(
                //     (widthF - circleRadius - paint.strokeWidth) + ((point2.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                //     (heightF - circleRadius - paint.strokeWidth) + ((point2.y + iconSize / 2) - (heightF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                //     circleRadius,
                //     normalPaint
                // )
                // canvas.drawCircle(
                //     (widthF - circleRadius - paint.strokeWidth) + ((point3.x + iconSize / 2) - (widthF - circleRadius - paint.strokeWidth)) / 10 * animationPos,
                //     point3.y + iconSize / 2,
                //    circleRadius,
                //    normalPaint
                // )
            }
            else -> {
                if (isOnTouched) {
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
                }
            }
        }
        
        when {
            isOnTouched -> drawButtonTouched(canvas)
            else -> drawButtonNormal(canvas)
        }
        
        // canvas.drawCircle(widthF - circleRadius - paint.strokeWidth, heightF - circleRadius - paint.strokeWidth, circleRadius, paint)
        // canvas.drawBitmap(plusDrawable.toBitmap(), widthF - circleRadius - paint.strokeWidth - iconSize / 2, widthF - circleRadius - paint.strokeWidth - iconSize / 2, paint)
    }
    
    /**
     ********************************************************************************************************
     *
     **/
    private fun drawButtonNormal(canvas: Canvas) {
        drawButtonBackgroundNormal(canvas, buttonBackgroundColorNormal, buttonBackgroundStrokeColorNormal)
        drawButtonBitmapNormal(canvas)
    }
    
    private fun drawButtonBackgroundNormal(canvas: Canvas, buttonBackgroundColorNormal: Int?, buttonBackgroundStrokeColorNormal: Int?) {
        drawButtonBackground(canvas, buttonBackgroundColorNormal)
        drawButtonBackgroundStroke(canvas, buttonBackgroundStrokeColorNormal)
    }
    
    private fun drawButtonBitmapNormal(canvas: Canvas) = canvas.drawBitmap(plusDrawable.toBitmap(), widthF - circleRadius - paint.strokeWidth - iconSize / 2, widthF - circleRadius - paint.strokeWidth - iconSize / 2, paint)
    
    private fun drawButtonTouched(canvas: Canvas) {
        drawButtonBackgroundTouched(canvas, buttonBackgroundColorTouched, buttonBackgroundStrokeColorTouched)
        drawButtonBitmapTouched(canvas)
    }
    
    private fun drawButtonBackgroundTouched(canvas: Canvas, buttonBackgroundColorTouched: Int?, buttonBackgroundStrokeColorTouched: Int?) {
        drawButtonBackground(canvas, buttonBackgroundColorTouched)
        drawButtonBackgroundStroke(canvas, buttonBackgroundStrokeColorTouched)
    }
    
    private fun drawButtonBitmapTouched(canvas: Canvas) = canvas.drawBitmap(plusTouchedDrawable.toBitmap(), widthF - circleRadius - paint.strokeWidth - iconSize / 2, widthF - circleRadius - paint.strokeWidth - iconSize / 2, paint)
    
    private fun drawButtonBackground(canvas: Canvas, backgroundColor: Int?) {
        backgroundColor?.let { color ->
            paint.style = Paint.Style.FILL
            paint.color = color
            canvas.drawCircle(widthF - circleRadius - paint.strokeWidth, heightF - circleRadius - paint.strokeWidth, circleRadius, paint)
        }
    }
    
    private fun drawButtonBackgroundStroke(canvas: Canvas, backgroundStrokeColor: Int?) {
        backgroundStrokeColor?.let { color ->
            paint.style = Paint.Style.STROKE
            paint.color = color
            canvas.drawCircle(widthF - circleRadius - paint.strokeWidth, heightF - circleRadius - paint.strokeWidth, circleRadius, paint)
        }
    }
    
    /**
     ***********************************Following are the drawing of icons***********************************
     *
     * All center axis of circles should shift to left if the circle located at right edge,
     * and vice versa, except the no edge is closed.
     * Otherwise, drawn circle will be incorrect, that part of circle disappear.
     **/
    private fun drawNormalPhone(canvas: Canvas) {
        drawNormalBackground(canvas, iconBackgroundColor, point1.x + iconSize / 2, point1.y + iconSize / 2)
        drawNormalBackgroundStroke(canvas, iconBackgroundStrokeColor, point1.x + iconSize / 2, point1.y + iconSize / 2)
        canvas.drawBitmap(phoneDrawable.toBitmap(), point1.x, point1.y, normalPaint)
    }
    
    private fun drawSelectedPhone(canvas: Canvas) {
        drawSelectedBackground(canvas, iconBackgroundSelectedColor, point1.x + iconSize / 2, point1.y + iconSize / 2)
        drawSelectedBackgroundStroke(canvas, iconBackgroundSelectedStrokeColor, point1.x + iconSize / 2, point1.y + iconSize / 2)
        canvas.drawBitmap(phoneSelectedDrawable.toBitmap(), point1.x, point1.y, selectedPaint)
    }
    
    private fun drawNormalMessage(canvas: Canvas) {
        drawNormalBackground(canvas, iconBackgroundColor, point2.x + iconSize / 2, point2.y + iconSize / 2)
        drawNormalBackgroundStroke(canvas, iconBackgroundStrokeColor, point2.x + iconSize / 2, point2.y + iconSize / 2)
        canvas.drawBitmap(messageDrawable.toBitmap(), point2.x, point2.y, normalPaint)
    }
    
    private fun drawSelectedMessage(canvas: Canvas) {
        drawSelectedBackground(canvas, iconBackgroundSelectedColor, point2.x + iconSize / 2, point2.y + iconSize / 2)
        drawSelectedBackgroundStroke(canvas, iconBackgroundSelectedStrokeColor, point2.x + iconSize / 2, point2.y + iconSize / 2)
        canvas.drawBitmap(messageSelectedDrawable.toBitmap(), point2.x, point2.y, selectedPaint)
    }
    
    private fun drawNormalBrowser(canvas: Canvas) {
        drawSelectedBackground(canvas, iconBackgroundColor, point3.x + iconSize / 2, point3.y + iconSize / 2)
        drawSelectedBackgroundStroke(canvas, iconBackgroundStrokeColor, point3.x + iconSize / 2, point3.y + iconSize / 2)
        canvas.drawBitmap(browserDrawable.toBitmap(), point3.x, point3.y, normalPaint)
    }
    
    private fun drawSelectedBrowser(canvas: Canvas) {
        drawSelectedBackground(canvas, iconBackgroundSelectedColor, point3.x + iconSize / 2, point3.y + iconSize / 2)
        drawSelectedBackgroundStroke(canvas, iconBackgroundSelectedStrokeColor, point3.x + iconSize / 2, point3.y + iconSize / 2)
        canvas.drawBitmap(browserSelectedDrawable.toBitmap(), point3.x, point3.y, selectedPaint)
    }
    
    private fun drawNormalBackground(canvas: Canvas, iconBackgroundColor: Int?,x: Float, y: Float) {
        if (iconBackgroundColor != null) {
            normalPaint.style = Paint.Style.FILL
            normalPaint.color = iconBackgroundColor
            canvas.drawCircle(x, y, circleRadius, normalPaint)
        }
    }
    
    private fun drawNormalBackgroundStroke(canvas: Canvas, iconBackgroundStrokeColor: Int?,x: Float, y: Float) {
        if (iconBackgroundStrokeColor != null) {
            normalPaint.style = Paint.Style.STROKE
            normalPaint.color = iconBackgroundStrokeColor
            canvas.drawCircle(x, y, circleRadius, normalPaint)
        }
    }
    
    private fun drawSelectedBackground(canvas: Canvas, iconBackgroundSelectedColor: Int?, x: Float, y: Float) {
        if (iconBackgroundSelectedColor != null) {
            selectedPaint.style = Paint.Style.FILL
            selectedPaint.color = iconBackgroundSelectedColor
            canvas.drawCircle(x, y, circleRadius, selectedPaint)
        }
    }
    
    private fun drawSelectedBackgroundStroke(canvas: Canvas, iconBackgroundSelectedStrokeColor: Int?, x: Float, y: Float) {
        if (iconBackgroundSelectedStrokeColor != null) {
            selectedPaint.style = Paint.Style.STROKE
            selectedPaint.color = iconBackgroundSelectedStrokeColor
            canvas.drawCircle(x, y, circleRadius, selectedPaint)
        }
    }
    
    /*********************************************************************************************************/
    
}