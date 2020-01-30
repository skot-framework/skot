package tech.skot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.components.ScreenViewImpl.Companion.SK_EXTRA_VIEW_KEY
import tech.skot.core.SKLog

fun AppCompatActivity.onCreateSK(savedInstanceState: Bundle?) {
    if (intent.hasExtra(SK_EXTRA_VIEW_KEY)) {
        val viewKey = intent.getLongExtra(SK_EXTRA_VIEW_KEY, -1)
        SKLog.d("---- onCreateSK hasExtra viewKey $viewKey")
        try {
            setContentView(ScreenViewImpl.getInstance(viewKey).inflate(layoutInflater, this, null))
        } catch (ex: Exception) {
            SKLog.e("onCreateSK -> No View for key $viewKey", ex)
            finish()
        }
    } else {
        val initialViewImplKey = ScreenViewImpl.initialViewImplKey
        if (initialViewImplKey != null && ScreenViewImpl.instances.containsKey(initialViewImplKey)) {
            SKLog.d("---- onCreateSK initialViewImplKey != null $initialViewImplKey")
            setContentView(ScreenViewImpl.getInstance(initialViewImplKey).inflate(layoutInflater, this, null))
        } else {
            val initialGetter = ScreenViewImpl.getInitialViewImpl
            if (initialGetter != null) {
                SKLog.d("---- onCreateSK initialGetter != null will create")
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


abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateSK(savedInstanceState)
    }


    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        onBackPressedAction?.invoke()
    }
}
