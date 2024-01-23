package com.bav.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.lang.Integer.max

typealias OnAmountChangedListener = (Int) -> Unit

class EditAmountView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    companion object {
        private const val DEFAULT_LINE_WIDTH = 2
        private const val DEFAULT_RECT_ROUND = 100f
        private const val DEFAULT_TEXT_SIZE_2 = 22
        private const val DEFAULT_TEXT_SIZE = 16
        private const val WIDTH = 90
        private const val HEIGHT = 34

        private const val PLUS_TEXT = "+"
        private const val MINUS_TEXT = "-"
    }

    private val linePaint = Paint().apply {
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = context.toPx(DEFAULT_TEXT_SIZE)
        textAlign = Paint.Align.CENTER
    }
    private val textPaint2 = Paint().apply {
        isAntiAlias = true
        textSize = context.toPx(DEFAULT_TEXT_SIZE_2)
        textAlign = Paint.Align.CENTER
    }

    private val rectF = RectF()

    private var listener: OnAmountChangedListener? = null


    private var text = ""
    private var minAmount = 0
    private var maxAmount = 0
    private var currentAmount = 0

    private var lineWidth: Float = 0f

    init {
        if (!isInEditMode) {
            initAttrs(attrs, defStyleAttr, defStyleRes)
        } else {
            currentAmount = 1
            minAmount = 0
            maxAmount = 100
            linePaint.color = getColor(com.bav.core.R.color.tangerine_two)
            lineWidth = context.toPx(DEFAULT_LINE_WIDTH)
            linePaint.strokeWidth = lineWidth
            val textColor = Color.WHITE
            textPaint2.color = textColor
            textPaint.color = textColor
        }
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }
    fun setMinAmount(value: Int) {
        this.minAmount = value
        invalidate()
    }
    fun setMaxAmount(value: Int) {
        this.maxAmount = value
        invalidate()
    }
    fun setCurrentAmount(value: Int) {
        this.currentAmount = value
        listener?.invoke(value)
        requestLayout()
        invalidate()
    }

    fun setEditAmountListener(listener: OnAmountChangedListener) {
        this.listener = listener
    }

    @SuppressLint("Recycle", "CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText, defStyleAttr, defStyleRes)

        text = typedArray.getString(R.styleable.EditAmountView_text) ?: ""
        minAmount = typedArray.getInteger(R.styleable.EditAmountView_minAmount, 0)
        maxAmount = typedArray.getInteger(R.styleable.EditAmountView_maxAmount, 0)

        linePaint.color = typedArray.getColor(R.styleable.EditAmountView_lineColor, getColor(com.bav.core.R.color.tangerine_two))
        lineWidth = typedArray.getDimension(
            R.styleable.EditAmountView_lineWidth,
            context.toPx(DEFAULT_LINE_WIDTH)
        )
        linePaint.strokeWidth = lineWidth

        val textColor = typedArray.getColor(R.styleable.EditAmountView_textColor, Color.WHITE)
        textPaint2.color = textColor
        textPaint.color = textColor
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val minHeight = suggestedMinimumHeight + paddingBottom + paddingTop

        val width = context.toPx(WIDTH).toInt()
        val height = context.toPx(HEIGHT).toInt()

        val designWidth = max(minWidth, width)
        val designHeight = max(minHeight, height)

        setMeasuredDimension(
            resolveSize(designWidth, widthMeasureSpec),
            resolveSize(designHeight, heightMeasureSpec),
        )
    }

    override fun onDraw(canvas: Canvas) {
        rectF.set(
            lineWidth,
            lineWidth,
            context.toPx(WIDTH) - lineWidth * 2,
            context.toPx(HEIGHT) - lineWidth * 2
        )
        canvas.drawRoundRect(rectF, DEFAULT_RECT_ROUND, DEFAULT_RECT_ROUND, linePaint)

        if (currentAmount > 0) {
            canvas.drawText(
                MINUS_TEXT,
                0,
                MINUS_TEXT.length,
                rectF.width() * 0.25f - lineWidth,
                rectF.centerY() - (textPaint2.descent() + textPaint2.ascent()) / 2,
                textPaint2
            )

            val currentAmountText =  currentAmount.toString()
            canvas.drawText(
                currentAmountText,
                0,
                currentAmountText.length,
                rectF.centerX() - lineWidth,
                rectF.centerY() - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint
            )

            canvas.drawText(
                PLUS_TEXT,
                0,
                PLUS_TEXT.length,
                rectF.width() * 0.8f - lineWidth,
                rectF.centerY() - (textPaint2.descent() + textPaint2.ascent()) / 2,
                textPaint2
            )
        } else {
            canvas.drawText(
                text,
                0,
                text.length,
                rectF.centerX() - lineWidth,
                rectF.centerY() - (textPaint.descent() + textPaint.ascent()) / 2,
                textPaint
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (currentAmount == 0) {
                    if (event.x in 0f..rectF.width() && event.y in 0f..rectF.height()) {
                        setCurrentAmount(1)
                        return true
                    }
                } else {
                    if (event.x in 0f..rectF.width() * 0.3f && event.y in 0f..rectF.height()) {
                        if (currentAmount - 1 >= minAmount) {
                            setCurrentAmount(currentAmount - 1)
                            return true
                        }
                    } else if (event.x in rectF.width() * 0.7f..rectF.width() && event.y in 0f..rectF.height()) {
                        if (currentAmount + 1 <= maxAmount) {
                            setCurrentAmount(currentAmount + 1)
                            return true
                        }
                    }
                }
                return false
            }
        }

        return false
    }


    private fun getColor(id: Int) = resources.getColor(id, this.resources.newTheme())

    private fun Context.toPx(dp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resources.displayMetrics
    )

}