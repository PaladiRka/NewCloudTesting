package com.mole.android.newcloudtesting.sizepanel

class SizePanelPresenter {

    var valueState = 16
        set(value) {
            field = value
            view?.apply {
                if (onStateChangeListener != null) {
                    assert(view?.stateKey != -1)
                    this.onStateChangeListener(Pair(stateKey, value))
                }
            }
        }

    private var view: SizePanelView? = null

    fun attachView(view: SizePanelView) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun onIncrease() {
        valueState++
    }

    fun onDecease() {
        valueState = if (valueState == 0) 0 else valueState - 1
    }

    fun onChange(state: Pair<Int, Int>) {
        if (state.first == view?.stateKey) {
            valueState = state.second
        }
    }
}
