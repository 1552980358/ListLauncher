package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.util.heightFloat
import sakuraba.saki.list.launcher.util.widthFloat
import sakuraba.saki.list.launcher.view.base.BaseView

class CropImageView: BaseView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    companion object {
        private const val MOVE_STATE_UP = 0
        private const val MOVE_STATE_X = 1
        private const val MOVE_STATE_Y = 2
        private const val MOVE_STATE_FREE = 3
    }
    
    private val boundaryPaint = Paint()
    private var bitmap: Bitmap? = null
    private var bitmapOrigin: Bitmap? = null
    private val circleRadius by lazy { resources.getDimension(R.dimen.crop_image_stroke_circle_radius) }
    private var moveState = MOVE_STATE_UP
    
    private var downX = 0F
    private var downStartX = 0F
    private var downY = 0F
    private var downStartY = 0F
    
    init {
        boundaryPaint.color = Color.WHITE
        boundaryPaint.strokeWidth = resources.getDimension(R.dimen.crop_image_stroke_width)
        
        widthMax = resources.displayMetrics.widthPixels.toFloat()
        heightMax = resources.displayMetrics.heightPixels.toFloat()
        
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    when {
                        event.x in (startX - circleRadius .. startX + circleRadius)
                            && event.y in (startY + cropHeight - circleRadius .. startY + cropHeight + circleRadius) -> {
                                moveState = MOVE_STATE_Y
                            }
                        event.x in (startX + cropWidth - circleRadius .. startX + cropWidth + circleRadius)
                            && event.y in (startY - circleRadius .. startY + circleRadius) -> {
                                moveState = MOVE_STATE_X
                            }
                        event.x in (0F .. startX + cropWidth) && event.y in (0F .. startY + cropHeight) -> {
                            moveState = MOVE_STATE_FREE
                            downX = event.x
                            downStartX = startX
                            downY = event.y
                            downStartY = startY
                        }
                        else -> { }
                    }
                }
                ACTION_UP -> {
                    moveState = MOVE_STATE_UP
                }
                ACTION_MOVE -> {
                    when (moveState) {
                        MOVE_STATE_Y -> {
                            val bitmap = bitmap ?: return@setOnTouchListener false
                            val originWidth = cropWidth
                            val originHeight = cropWidth
                            cropHeight = when {
                                event.y - startY < 100 || event.y < startY -> 100F
                                event.y - startY > bitmap.height -> bitmap.height.toFloat()
                                else -> event.y - startY
                            }
                            cropWidth = widthMax * cropHeight / heightMax
    
                            // Modify the crop height and width, prevent over-height or over-width
                            // Check width
                            if (cropWidth > bitmap.width) {
                                cropWidth = bitmap.width.toFloat()
                                cropHeight = heightMax * cropWidth / widthMax
                            }
                            // Check x and y end point
                            if (cropWidth > originWidth && startX + cropWidth > bitmap.width ||
                                cropHeight > originHeight && startY + cropHeight > bitmap.height) {
                                cropWidth = originWidth
                                cropHeight = originHeight
                            }
                        }
                        MOVE_STATE_X -> {
                            val bitmap = bitmap ?: return@setOnTouchListener false
                            val originWidth = cropWidth
                            val originHeight = cropHeight
                            cropWidth = when {
                                event.x - startX < 100 || event.x < startX -> 100F
                                event.x - startX > bitmap.width -> bitmap.width.toFloat()
                                else -> event.x - startX
                            }
                            cropHeight = heightMax * cropWidth / widthMax
                            // Modify the crop height and width, prevent over-height or over-width
                            // Check height
                            if (cropHeight > bitmap.height) {
                                cropHeight = bitmap.height.toFloat()
                                cropWidth = widthMax * cropHeight / heightMax
                            }
                            // Check x and y end point
                            if (cropWidth > originWidth && startX + cropWidth > bitmap.width ||
                                cropHeight > originHeight && startY + cropHeight > bitmap.height) {
                                cropWidth = originWidth
                                cropHeight = originHeight
                            }
                        }
                        MOVE_STATE_FREE -> {
                            val bitmap = bitmap?:return@setOnTouchListener true
                            // var diff: Float
                            startX = when {
                                event.x < 0 -> 0F
                                event.x >= bitmap.width -> bitmap.width - cropWidth
                                else -> when  {
                                    (event.x > downX) -> when {
                                        (event.x - downX >= bitmap.width - downStartX - cropWidth) ->
                                            bitmap.width - cropWidth
                                        else -> event.x - (downX - downStartX)
                                    }
                                    else -> when {
                                        (downStartX <= downX - event.x) -> 0F
                                        else -> downStartX - (downX - event.x)
                                    }
                                }
                                /**
                                 * ***************************
                                 * *****DEPRECATED METHOD*****
                                 * ***************************
                                 * The frame may move suddenly to the left or right
                                 * even you just move a little distance.
                                 * With optimized code, the movement of the frame is much
                                 * better
                                 *
                                 * if (event.x > downX) {
                                 *     diff = event.x - downX
                                 *         if (bitmap.width - startX - cropWidth > diff) {
                                 *             startX += diff
                                 *         } else {
                                 *             startX = bitmap.width - cropWidth
                                 *         }
                                 * } else {
                                 *     diff = downX - event.x
                                 *     if (startX >= diff) {
                                 *         startX -= diff
                                 *     } else {
                                 *         startX = 0F
                                 *     }
                                 * }
                                 **/
                            }
                            startY = when {
                                event.y < 0 -> 0F
                                event.y >= bitmap.height -> bitmap.height - cropHeight
                                else -> {
                                    when  {
                                        (event.y > downY) -> when {
                                            (event.y - downY >= bitmap.height - downStartY - cropHeight) ->
                                                bitmap.heightFloat - cropHeight
                                            else -> event.y - (downY - downStartY)
                                        }
                                        else -> when {
                                            (downStartY <= downY - event.y) -> 0F
                                            else -> downStartY - (downY - event.y)
                                        }
                                    }
                                    /**
                                     * ***************************
                                     * *****DEPRECATED METHOD*****
                                     * ***************************
                                     * The frame may move suddenly to the left or right
                                     * even you just move a little distance.
                                     * With optimized code, the movement of the frame is much
                                     * better
                                     *
                                     * if (event.y > downY) {
                                     *     diff = event.y - downY
                                     *     if (bitmap.height - startY - cropHeight >= diff) {
                                     *         startY += diff
                                     *     } else {
                                     *         startY = bitmap.height - cropHeight
                                     *     }
                                     * } else {
                                     *     diff = downY - event.y
                                     *     if (startY >= diff) {
                                     *         startY -= diff
                                     *     } else {
                                     *         startY = 0F
                                     *     }
                                     * }
                                     **/
                                }
                            }
                        }
                    }
                    invalidate()
                }
            }
            return@setOnTouchListener true
        }
    }
    
    private var isViewMeasured = false
    private var isBitmapResized = false
    
    private var widthMax = 0F
    private var heightMax = 0F
    
    private var startX = 0F
    private var startY = 0F
    
    private var cropWidth = 0F
    private var cropHeight = 0F
    
    @Synchronized
    private fun getPictureResized(bitmap: Bitmap?) {
        bitmap ?: return
    
        /**
         * The following direct resizing will cause the [Bitmap.getHeight] or [Bitmap.getWidth]
         * larger than the [CropImageView.getHeight] or [CropImageView.getWidth]
         * of this [CropImageView], respectively, causing the [bitmap] cannot be shown fully.
         *
         * *******************************************************************
         * ******The picture should be resized twice to fix the problem.******
         * *******************************************************************
         *
         * val newBitmap = Bitmap.createBitmap(
         *     bitmap,
         *     0,
         *     0,
         *     bitmap.width,
         *     bitmap.height,
         *     Matrix().apply {
         *         when {
         *             bitmap.height > bitmap.width -> {
         *                 (heightFloat / bitmap.height).apply {
         *                     setScale(this, this)
         *                     cropHeight = heightFloat
         *                     cropWidth = widthMax * (cropHeight / heightMax)
         *                 }
         *             }
         *             else -> {
         *                 (widthFloat / bitmap.width).apply { setScale(this, this) }
         *                 cropWidth = widthFloat
         *                 cropHeight = heightMax * (cropWidth / widthMax)
         *             }
         *         }
         *     },
         *     false
         * )
         *
         * this.bitmap = newBitmap
         **/
        
        // Calculate out all data for further processing
        var bitmapWidth = bitmap.widthFloat
        var bitmapHeight = bitmap.heightFloat
        
        //
        when {
            bitmapWidth < bitmapHeight -> {
                bitmapHeight *= width / bitmapWidth
                bitmapWidth = widthFloat
            }
            // bitmapWidth <= bitmapHeight
            else -> {
                bitmapWidth *= height / bitmapHeight
                bitmapHeight = heightFloat
                
            }
        }
    
        /**
         * Then check [bitmapHeight] and [bitmapWidth] whether they are larger than
         * [CropImageView.getHeight] or [CropImageView.getWidth]
         **/
        if (bitmapHeight > height) {
            bitmapWidth *= heightFloat / bitmapHeight
            bitmapHeight = heightFloat
        }
        if (bitmapWidth > width) {
            bitmapHeight *= width / bitmapWidth
            bitmapWidth = widthFloat
        }
        
        when {
            bitmapWidth > bitmapHeight -> {
                cropWidth = bitmapWidth
                cropHeight = heightMax * cropWidth / widthMax
            }
            else -> {
                cropHeight = bitmapHeight
                cropWidth = widthMax * cropHeight / heightMax
            }
        }
        
        this.bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth.toInt(), bitmapHeight.toInt(), false)
        isBitmapResized = true
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        isViewMeasured = true
    }
    
    @Synchronized
    fun setBitmap(uri: String) {
        bitmapOrigin = BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(uri)))
        if (isViewMeasured) {
            getPictureResized(bitmapOrigin)
        }
        invalidate()
    }
    
    fun getCutBitmap(): Bitmap? {
        bitmapOrigin ?: return null
        bitmap ?: return null
        val bitmapOrigin = bitmapOrigin!!
        val bitmap = bitmap!!
        /**
         * Some pixels at the right disappear by unknown reason.
         * So, use [Bitmap.createBitmap] instead
         *
         * val cutWidth = (bitmapOrigin.width * (cropWidth / bitmap.width)).toInt()
         * val cutHeight = (bitmapOrigin.height * (cropHeight / bitmap.height)).toInt()
         * val startXInt = (bitmapOrigin.width * (startX / bitmap.width)).toInt()
         * val startYInt = (bitmapOrigin.height * (startY / bitmap.height)).toInt()
         *
         * val emptyBitmap = Bitmap.createBitmap(cutWidth, cutHeight, Bitmap.Config.ARGB_8888)
         * val canvas = Canvas(emptyBitmap)
         * canvas.drawBitmap(bitmapOrigin, Rect(startXInt, startYInt, cutWidth, cutHeight), Rect(0, 0, cutWidth, cutHeight), null)
         **/
        
        return Bitmap.createBitmap(
            bitmapOrigin,
            (bitmapOrigin.width * (startX / bitmap.width)).toInt(),
            (bitmapOrigin.height * (startY / bitmap.height)).toInt(),
            (bitmapOrigin.width * (cropWidth / bitmap.width)).toInt(),
            (bitmapOrigin.height * (cropHeight / bitmap.height)).toInt()
        )
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        bitmapOrigin?:return
        
        if (!isBitmapResized) {
            getPictureResized(bitmapOrigin)
        }
        val bitmap = bitmap!!
        canvas.drawBitmap(bitmap, 0F, 0F, paint)
        
        // Draw lines
        boundaryPaint.style = Paint.Style.STROKE
        // Left
        canvas.drawLine(startX, startY, startX, startY + cropHeight, boundaryPaint)
        // Upper
        canvas.drawLine(startX - (boundaryPaint.strokeWidth / 2), startY, startX + cropWidth, startY, boundaryPaint)
        // Right
        canvas.drawLine(startX + cropWidth, startY, startX + cropWidth, startY + cropHeight, boundaryPaint)
        // Bottom
        canvas.drawLine(startX - (boundaryPaint.strokeWidth / 2), startY + cropHeight, startX + cropWidth + (boundaryPaint.strokeWidth / 2), startY + cropHeight, boundaryPaint)
    
        // Draw circles
        boundaryPaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(startX + cropWidth, startY, resources.getDimension(R.dimen.crop_image_stroke_circle_radius), boundaryPaint)
        canvas.drawCircle(startX,startY + cropHeight, resources.getDimension(R.dimen.crop_image_stroke_circle_radius), boundaryPaint)
        
    }
    
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
    
}