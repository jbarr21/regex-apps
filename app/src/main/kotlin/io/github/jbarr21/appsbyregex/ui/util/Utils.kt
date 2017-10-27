package io.github.jbarr21.appsbyregex.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AnyRes
import android.support.annotation.DrawableRes
import android.util.TypedValue

fun <T> Int.ifApiAtLeast(func: (T) -> Unit, param: T) {
    if (Build.VERSION.SDK_INT >= this) {
        func(param)
    }
}

@DrawableRes
fun Context.resolveDrawableId(@AnyRes resId: Int): Int {
    val outValue = TypedValue()
    theme.resolveAttribute(resId, outValue, true)
    return outValue.resourceId
}

fun Drawable.toBitmap(): Bitmap = if (this is BitmapDrawable) {
    this.bitmap
} else {
    val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    bitmap
}
