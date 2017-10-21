package io.github.jbarr21.appsbyregex.ui.main

import android.content.SharedPreferences
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.support.design.widget.RxAppBarLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposeWith
import dagger.android.AndroidInjection
import io.github.jbarr21.appsbyregex.BuildConfig
import io.github.jbarr21.appsbyregex.R
import io.github.jbarr21.appsbyregex.data.AppManager
import io.github.jbarr21.appsbyregex.ui.util.ifApiAtLeast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        val KEY_REGEX = "KEY_REGEX"
        val REGEX_DEFAULT = "(com|io).*regex"
        val DEBOUNCE_MS = 300L
        val MAX_SHORTCUTS = 4
    }

    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var appManager: AppManager
    @Inject lateinit var adapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (BuildConfig.FLAVOR != "other") {
            supportActionBar?.title = "${getString(R.string.app_name)} (${BuildConfig.FLAVOR.capitalize()})"
        }

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        val initialRegex = prefs.getString(KEY_REGEX, REGEX_DEFAULT)
        regex_edittext.setText(initialRegex)

        RxAppBarLayout.offsetChanges(appbar).subscribe({toolbar.alpha = (-it / toolbar.top.toFloat())})

        RxView.clicks(fab)
                .doOnNext { VERSION_CODES.N_MR1.ifApiAtLeast(appManager::updateAppShortcuts, adapter.items) }
                .subscribe { Toast.makeText(this, "Shortcuts updated!", Toast.LENGTH_SHORT).show(); }

        RxTextView.textChanges(regex_edittext)
            .startWith(initialRegex)
            .observeOn(Schedulers.io())
            .debounce(DEBOUNCE_MS, TimeUnit.MILLISECONDS)
            .doOnNext({ regex -> prefs.edit().putString(KEY_REGEX, regex.toString()).apply() })
            .switchMap { appManager.fetchApps(it.toString()) }
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposeWith(AndroidLifecycleScopeProvider.from(this))
            .subscribe({
                adapter.setItems(it)
                progress.visibility = View.GONE
            })
    }
}
