package com.mole.android.newcloudtesting

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.minus


class MyTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var text = ""
        set(value) {
            field = value
            invalidate()
        }

    private var ignoreMoveEvents = false

    private val currentTextPoint = PointF(-1f, -1f)
    private val lastTouchPoint = PointF()

    private val textPaint = TextPaint()

    private lateinit var textLayout: StaticLayout

    private var lastState: Int = MotionEvent.ACTION_UP

    init {
        textPaint.isAntiAlias = true
        textPaint.textSize = 16 * resources.displayMetrics.density
        textPaint.color = -0x1000000
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.MyTextView,
                0,
                0
            )
            val stateString =
                try {
                    typedArray.getString(R.styleable.MyTextView_text)
                } finally {
                    typedArray.recycle()
                }
            this.text = stateString ?: ""
        }
    }

    @SuppressLint("WrongConstant")
    fun setBold(isBold: Boolean) {
        // Get current style without Bold
        val style = textPaint.typeface.style and Typeface.BOLD.inv()

        val effectMask = if (isBold) Typeface.BOLD.or(style) else style

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, effectMask)
        invalidate()
    }

    @SuppressLint("WrongConstant")
    fun setItalic(isItalic: Boolean) {
        // Get current style without Italic
        val style = textPaint.typeface.style and Typeface.ITALIC.inv()

        val effectMask = if (isItalic) Typeface.ITALIC.or(style) else style

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, effectMask)
        invalidate()
    }

    fun setSize(size: Int) {
        textPaint.textSize = size * resources.displayMetrics.density
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textLayoutBuild()

        currentTextPoint.inRange(
            0f,
            0f,
            measuredWidth.toFloat() - textLayout.width,
            measuredHeight.toFloat() - textLayout.height
        )

        canvas.save()
        canvas.translate(currentTextPoint.x, currentTextPoint.y)
        textLayout.draw(canvas)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (currentTextPoint.equals(-1f, -1f)) {
            toBaseStateText()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                if (!ignoreMoveEvents) {
                    val currentMovePoint = PointF()
                    currentMovePoint.y = event.y
                    currentMovePoint.x = event.x
                    val deltaPoint = currentMovePoint.minus(lastTouchPoint)
                    lastTouchPoint.set(currentMovePoint)
                    currentTextPoint.offset(deltaPoint)

                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (lastState == MotionEvent.ACTION_DOWN) {
                    toBaseStateText()
                    invalidate()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                lastTouchPoint.y = event.y
                lastTouchPoint.x = event.x
            }
        }
        lastState = event.actionMasked
        return true
    }

    private fun textLayoutBuild() {
        val textWidth = textPaint.measureText(text)
        val residualWidth = measuredWidth - currentTextPoint.x
        val width = kotlin.math.min(textWidth, residualWidth)

        val widthFinal = kotlin.math.max(width, textPaint.textSize)

        val sb = StaticLayout.Builder
            .obtain(text, 0, text.length, textPaint, widthFinal.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setIncludePad(false)
        textLayout = sb.build()
    }

    private fun toBaseStateText() {
        val width = textPaint.measureText(text)
        currentTextPoint.x = measuredWidth.toFloat() / 2 - width / 2
        currentTextPoint.y = measuredHeight.toFloat() / 2
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        if (state.stateToSave != null) {
            this.currentTextPoint.set(state.stateToSave!!)
        } else {
            toBaseStateText()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.stateToSave = this.currentTextPoint
        return ss
    }

    internal class SavedState(superState: Parcelable?) : BaseSavedState(superState) {
        var stateToSave: PointF? = PointF()

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeParcelable(stateToSave, flags)
        }
    }
}
