package io.github.jbarr21.appsbyregex.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.jbarr21.appsbyregex.ui.main.MainActivity
import io.github.jbarr21.appsbyregex.ui.main.MainActivityModule

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun bindMainActivity(): MainActivity
}
