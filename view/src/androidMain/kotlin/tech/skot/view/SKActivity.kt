package tech.skot.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.components.ScreenViewImpl.Companion.SK_EXTRA_VIEW_KEY
import tech.skot.core.SKLog

fun AppCompatActivity.onCreateSK(savedInstanceState: Bundle?):ScreenViewImpl<*,*,*>? {
    if (intent.hasExtra(SK_EXTRA_VIEW_KEY)) {
        val viewKey = intent.getLongExtra(SK_EXTRA_VIEW_KEY, -1)
        return try {
            val screenViewImpl = ScreenViewImpl.getInstance(viewKey)!!
            setContentView(screenViewImpl.inflate(layoutInflater, this, null))
            screenViewImpl
        } catch (ex: Exception) {
            SKLog.e("onCreateSK -> No View for key $viewKey", ex)
            finish()
            null
        }
    } else {
        val action = intent?.action
        val encodedPath = intent?.data?.encodedPath

        if (action != null && action == Intent.ACTION_VIEW && encodedPath != null) {
            val screenKey = ScreenViewImpl.onLink?.invoke(encodedPath, intent?.data?.encodedFragment)
            return if (screenKey != null) {
                val screenViewImpl = ScreenViewImpl.getInstance(screenKey)!!
                setContentView(screenViewImpl.inflate(layoutInflater, this, null))
                screenViewImpl
            } else {
                SKLog.d("----screen pas trouvé  -> will finish in 1 s ")
                Handler().postDelayed({
                    finish()
                }, 1000)
                null
            }

        } else {
            val initialViewImplKey = ScreenViewImpl.initialViewImplKey
            return if (initialViewImplKey != null && ScreenViewImpl.instances.containsKey(initialViewImplKey)) {
                val screenViewImpl = ScreenViewImpl.getInstance(initialViewImplKey)!!
                setContentView(screenViewImpl.inflate(layoutInflater, this, null))
                screenViewImpl
            } else {
                val initialGetter = ScreenViewImpl.getInitialViewImpl
                if (initialGetter != null) {
                    val initialViewImpl = initialGetter()
                    ScreenViewImpl.initialViewImplKey = initialViewImpl.key
                    setContentView(initialViewImpl.inflate(layoutInflater, this, null))
                    initialViewImpl
                } else {
                    SKLog.e("Please set ScreenViewImpl.getInitialViewImpl", IllegalStateException("ScreenViewImpl.getInitialViewImpl not set"))
                    finish()
                    null
                }
            }
        }


    }


}


abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenViewImpl = realOnCreate(savedInstanceState)
    }

    private var screenViewImpl:ScreenViewImpl<*,*,*>? = null

    open fun realOnCreate(savedInstanceState: Bundle?):ScreenViewImpl<*,*,*>? {
        return onCreateSK(savedInstanceState)
    }


    override fun onDestroy() {
        super.onDestroy()
        screenViewImpl?.cleanViewReferences()
    }

    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        onBackPressedAction?.invoke()
    }


}

