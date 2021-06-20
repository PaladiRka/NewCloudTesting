package com.mole.android.newcloudtesting.sizepanel

import com.mole.android.newcloudtesting.OnStateChangeListener
import com.mole.android.newcloudtesting.StateChange

interface SizePanelView: StateChange, OnStateChangeListener {

    fun onStateChangeListener(state: Pair<Int, Int>)

}
