package com.mole.android.newcloudtesting

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.forEach


class MyStylePanel @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), OnStateChange,
    OnStateChangeListener {

    val states: MutableMap<Int, Int> = HashMap()

    override var onStateChangeListener: OnStateChange? = null

    init {
        inflate(context, R.layout.view_style_panel, this)

        forEach { view ->
            if (view is OnStateChangeListener) {
                view.onStateChangeListener = this
            }
        }
    }

    override fun onStateChange(v: View, state: Pair<Int, Int>) {
        val key = state.first
        val value = state.second
        states[key] = value

        onStateChangeListener?.onStateChange(this, state)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        this.states.putAll(state.stateToSave)
        forEach { view ->
            if (view is StateChange) {
                val stateValue = states[view.stateKey]!!
                val initState = Pair(view.stateKey, stateValue)
                view.stateChange(initState)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.stateToSave = this.states
        return ss
    }

    internal class SavedState(superState: Parcelable?) : BaseSavedState(superState) {
        var stateToSave: MutableMap<Int, Int> = HashMap()

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(stateToSave.size)
            stateToSave.forEach { state ->
                out.writeInt(state.key)
                out.writeInt(state.value)
            }
        }
    }
}
