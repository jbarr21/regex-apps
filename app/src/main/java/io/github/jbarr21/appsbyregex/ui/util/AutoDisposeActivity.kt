package io.github.jbarr21.appsbyregex.ui.util

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.autodispose.LifecycleEndedException
import com.uber.autodispose.LifecycleScopeProvider

import io.reactivex.Observable
import io.reactivex.functions.Function

open class AutoDisposeActivity : AppCompatActivity(), LifecycleScopeProvider<AutoDisposeActivity.ActivityEvent> {

    private val lifecycleRelay = BehaviorRelay.create<ActivityEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRelay.accept(ActivityEvent.CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRelay.accept(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleRelay.accept(ActivityEvent.RESUME)
    }

    override fun onPause() {
        super.onPause()
        lifecycleRelay.accept(ActivityEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycleRelay.accept(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRelay.accept(ActivityEvent.DESTROY)
    }

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleRelay.hide()
    }

    override fun correspondingEvents(): Function<ActivityEvent, ActivityEvent> {
        return ActivityEvent.LIFECYCLE
    }

    override fun peekLifecycle(): ActivityEvent? {
        return lifecycleRelay.value
    }

    enum class ActivityEvent {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY;

        companion object {

            /**
             * Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities.
             */
            val LIFECYCLE: Function<ActivityEvent, ActivityEvent> = Function { lastEvent ->
                when (lastEvent) {
                    CREATE -> return@Function DESTROY
                    START -> return@Function STOP
                    RESUME -> return@Function PAUSE
                    PAUSE -> return@Function STOP
                    STOP -> return@Function DESTROY
                    DESTROY -> throw LifecycleEndedException("Cannot bind to Activity lifecycle when outside of it.")
                }
                throw UnsupportedOperationException("Binding to $lastEvent not yet implemented")
            }
        }
    }
}
