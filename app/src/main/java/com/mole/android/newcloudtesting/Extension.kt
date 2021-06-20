package com.mole.android.newcloudtesting

import android.content.Context
import android.graphics.PointF
import android.util.TypedValue
import androidx.annotation.AttrRes

fun PointF.offset(diff: PointF) {
    this.x += diff.x
    this.y += diff.y
}

fun PointF.inRange(lowerX: Float, lowerY: Float, highX: Float, highY: Float) {
    this.x = if (this.x < lowerX) lowerX else if (this.x > highX) highX else this.x
    this.y = if (this.y < lowerX) lowerY else if (this.y > highY) highY else this.y
}

fun Context.resolveColor(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

fun Int.toBoolean(): Boolean {
    return this != 0
}
