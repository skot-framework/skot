package tech.skot.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.components.ScreenViewImpl.Companion.SK_EXTRA_VIEW_KEY
import tech.skot.core.SKLog

fun AppCompatActivity.onCreateSK(savedInstanceState: Bundle?) {
    if (intent.hasExtra(SK_EXTRA_VIEW_KEY)) {
        val viewKey = intent.getLongExtra(SK_EXTRA_VIEW_KEY, -1)
        try {
            setContentView(ScreenViewImpl.getInstance(viewKey)!!.inflate(layoutInflater, this, null))
        } catch (ex: Exception) {
            SKLog.e("onCreateSK -> No View for key $viewKey", ex)
            finish()
        }
    } else {
        val action = intent?.action
        val encodedPath = intent?.data?.encodedPath

        if (action != null && action == Intent.ACTION_VIEW && encodedPath != null) {
            SKLog.d("----link encodedPath $encodedPath")
            val screenKey = ScreenViewImpl.onLink?.invoke(encodedPath)
            if (screenKey != null) {
                setContentView(ScreenViewImpl.getInstance(screenKey)!!.inflate(layoutInflater, this, null))
            } else {
                SKLog.d("----screen pas trouvé  -> will finish in 1 s ")
                Handler().postDelayed({
                    finish()
                }, 1000)
            }

        } else {
            val initialViewImplKey = ScreenViewImpl.initialViewImplKey
            if (initialViewImplKey != null && ScreenViewImpl.instances.containsKey(initialViewImplKey)) {
                setContentView(ScreenViewImpl.getInstance(initialViewImplKey)!!.inflate(layoutInflater, this, null))
            } else {
                val initialGetter = ScreenViewImpl.getInitialViewImpl
                if (initialGetter != null) {
                    val initialViewImpl = initialGetter()
                    ScreenViewImpl.initialViewImplKey = initialViewImpl.key
                    setContentView(initialViewImpl.inflate(layoutInflater, this, null))
                } else {
                    SKLog.e("Please set ScreenViewImpl.getInitialViewImpl", IllegalStateException("ScreenViewImpl.getInitialViewImpl not set"))
                    finish()
                }
            }
        }


    }


}


abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realOnCreate(savedInstanceState)
    }

    open fun realOnCreate(savedInstanceState: Bundle?) {
        onCreateSK(savedInstanceState)
    }

    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        onBackPressedAction?.invoke()
    }
}
