package com.mole.android.newcloudtesting.sizepanel

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.mole.android.newcloudtesting.OnStateChange
import com.mole.android.newcloudtesting.R

class SizePanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), SizePanelView {

    override var stateKey: Int = -1

    override var onStateChangeListener: OnStateChange? = null
        set(value) {
            field = value
            if (value != null) {
                assert(stateKey != -1)
                value.onStateChange(this, Pair(stateKey, presenter.valueState))
            }
        }

    private val presenter = SizePanelPresenter()

    private val increaseImageButton: AppCompatImageButton
    private val decreaseImageButton: AppCompatImageButton

    init {
        inflate(context, R.layout.view_size_button, this)

        increaseImageButton = findViewById(R.id.increaseButton)
        increaseImageButton.setOnClickListener {
            presenter.onIncrease()
        }

        decreaseImageButton = findViewById(R.id.decreaseButton)
        decreaseImageButton.setOnClickListener {
            presenter.onDecease()
        }

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.SizePanel,
                0,
                0
            )
            val state = typedArray.getInt(R.styleable.SizePanel_state, -1)
            typedArray.recycle()
            this.stateKey = state
        }
        presenter.attachView(this)
    }

    override fun onStateChangeListener(state: Pair<Int, Int>) {
        onStateChangeListener?.onStateChange(this, state)
    }

    override fun stateChange(state: Pair<Int, Int>) {
        presenter.onChange(state)
    }
}
