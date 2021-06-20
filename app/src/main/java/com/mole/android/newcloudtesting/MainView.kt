package com.mole.android.newcloudtesting

interface MainView {
    fun setBold(isBold: Boolean)

    fun setItalic(isItalic: Boolean)

    fun getState(): MutableMap<Int, Int>

    fun setSize(size: Int)

}
