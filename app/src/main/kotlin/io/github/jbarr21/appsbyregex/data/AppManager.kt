package io.github.jbarr21.appsbyregex.data

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import io.github.jbarr21.appsbyregex.ui.main.MainActivity
import io.github.jbarr21.appsbyregex.ui.util.toBitmap
import io.reactivex.Observable
import java.util.regex.PatternSyntaxException

class AppManager(private val context: Context) {

    private val packageManager: PackageManager by lazy { context.applicationContext.packageManager }
    private val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
    private val cache: MutableCollection<App> = mutableListOf()

    fun fetchApps(regex: String): Observable<Collection<App>> = Observable.just(
            try {
                packageManager.queryIntentActivities(mainIntent, 0)
                    .map { it.activityInfo.applicationInfo }
                    .filter { it.packageName.matches(Regex(".*$regex.*")) }
                    .map {
                        App(
                                it.loadLabel(packageManager).toString(),
                                it.packageName,
                                it.loadIcon(packageManager))
                    }
                    .toSortedSet()
            } catch (e: PatternSyntaxException) {
                cache.toList()
            }
        ).onExceptionResumeNext { cache }
        .doOnNext { it ->
            cache.clear()
            cache.addAll(it)
        }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    fun updateAppShortcuts(apps: Iterable<App>) {
        val shortcuts = apps
                .take(MainActivity.MAX_SHORTCUTS)
                .map { app ->
                    ShortcutInfo.Builder(context, "${app.pkg}.${app.name}")
                            .setShortLabel(app.name)
                            .setLongLabel(app.name)
                            .setIcon(Icon.createWithBitmap(app.icon.toBitmap()))
                            .setIntent(packageManager.getLaunchIntentForPackage(app.pkg))
                            .build()
                }
                .toList()
        context.getSystemService(ShortcutManager::class.java).dynamicShortcuts = shortcuts
    }
}
