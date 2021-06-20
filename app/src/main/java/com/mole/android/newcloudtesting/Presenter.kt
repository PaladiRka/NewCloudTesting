package com.mole.android.newcloudtesting


class Presenter {

    private var view: MainActivity? = null

    fun attachView(attachedView: MainActivity) {
        view = attachedView
    }

    fun detachView() {
        view = null
    }

    fun onStateChange() {
        view?.apply {
            getState().forEach { state ->
                when (state.key) {
                    BOLD -> {
                        setBold(state.value.toBoolean())
                    }
                    ITALIC -> {
                        setItalic(state.value.toBoolean())
                    }
                    SIZE -> {
                        setSize(state.value)
                    }
                }
            }
        }
    }

    companion object{
        private const val BOLD = 0
        private const val ITALIC = 1
        private const val SIZE = 2
    }

}
