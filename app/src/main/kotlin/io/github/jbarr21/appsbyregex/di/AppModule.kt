package io.github.jbarr21.appsbyregex.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import io.github.jbarr21.appsbyregex.data.AppManager
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app

    @Provides
    @Singleton
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences("apps-by-regex", AppCompatActivity.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePackageManager(context: Context) = context.applicationContext.packageManager

    @Provides
    @Singleton
    fun provideAppManager(context: Context) = AppManager(context.applicationContext)
}
