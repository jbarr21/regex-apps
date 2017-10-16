package io.github.jbarr21.appsbyregex.data

import android.graphics.drawable.Drawable

data class App(val name: String, val pkg: String, val icon: Drawable) : Comparable<App> {
    override fun compareTo(other: App): Int {
        val result = name.toLowerCase().compareTo(other.name.toLowerCase())
        return if (result != 0) result else pkg.compareTo(other.pkg)
    }
}
