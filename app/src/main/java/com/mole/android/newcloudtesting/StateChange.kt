package com.mole.android.newcloudtesting

import android.view.View


interface OnStateChangeListener {
    var onStateChangeListener: OnStateChange?
}

fun interface OnStateChange {
    fun onStateChange(v: View, state: Pair<Int, Int>)
}

interface StateChange {

    val stateKey: Int

    fun stateChange(state: Pair<Int, Int>)
}
