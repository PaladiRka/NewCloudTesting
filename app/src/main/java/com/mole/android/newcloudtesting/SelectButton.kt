package com.mole.android.newcloudtesting

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton


class SelectButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.imageButtonStyle
) : AppCompatImageButton(context, attrs, defStyleAttr), OnStateChangeListener, StateChange {

    override var stateKey: Int = -1

    override var onStateChangeListener: OnStateChange? = null
        set(value) {
            field = value
            if (value != null) {
                assert(stateKey != -1)
                value.onStateChange(this, Pair(stateKey, isClick.toInt()))
            }
        }

    private var isClick = false
        set(value) {
            field = value
            if (onStateChangeListener != null) {
                assert(stateKey != -1)
                onStateChangeListener?.onStateChange(this, Pair(stateKey, value.toInt()))
            }
            updateTint()
        }

    private var selectColor = context.resolveColor(R.attr.colorSelect)
    private var defaultColor = context.resolveColor(R.attr.colorDefault)

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.SelectButton,
                0,
                0
            )
            val state = typedArray.getInt(R.styleable.SelectButton_state, -1)
            typedArray.recycle()
            this.stateKey = state
        }
    }

    override fun performClick(): Boolean {
        isClick = !isClick
        return super.performClick()
    }

    override fun stateChange(state: Pair<Int, Int>) {
        if (state.first == this.stateKey) {
            isClick = state.second.toBoolean()
        }
    }

    private fun updateTint() {
        imageTintList = if (isClick) {
            ColorStateList.valueOf(selectColor)
        } else {
            ColorStateList.valueOf(defaultColor)
        }
    }
}
