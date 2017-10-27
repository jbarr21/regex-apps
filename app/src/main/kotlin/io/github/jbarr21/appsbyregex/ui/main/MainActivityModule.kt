package io.github.jbarr21.appsbyregex.ui.main

import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import io.github.jbarr21.appsbyregex.data.App

@Module
class MainActivityModule {

    @Provides
    fun provideAppAdapter(onAppClicked: OnAppItemClicked): AppAdapter {
        return AppAdapter(emptyList<App>().toMutableList(), onAppClicked)
    }

    @Provides
    fun provideOnAppItemClicked(activity: MainActivity, pm: PackageManager): OnAppItemClicked {
        return object : OnAppItemClicked {
            override fun onAppClicked(app: App) {
                activity.run {
                    finish()
                    startActivity(pm.getLaunchIntentForPackage(app.pkg))
                }
            }
        }
    }
}
