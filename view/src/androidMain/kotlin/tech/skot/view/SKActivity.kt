package tech.skot.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.components.ScreenViewImpl.Companion.SK_EXTRA_VIEW_KEY
import tech.skot.components.ScreenViewImpl.Companion.launchActivityClass
import tech.skot.core.SKLog


abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateSK(savedInstanceState)
    }


    private var screenViewImpl: ScreenViewImpl<*, *, *>? = null


    fun getKeyForThisActivity(savedInstanceState: Bundle?) =
            when {
                intent.hasExtra(SK_EXTRA_VIEW_KEY) -> intent.getLongExtra(SK_EXTRA_VIEW_KEY, -1)
                savedInstanceState?.containsKey(SK_EXTRA_VIEW_KEY) == true -> savedInstanceState.getLong(SK_EXTRA_VIEW_KEY, -1)
                else -> -1
            }


    fun onCreateSK(savedInstanceState: Bundle?) {
        val viewKey = getKeyForThisActivity(savedInstanceState)
        if (viewKey != -1L && ScreenViewImpl.oneActivityAlreadyLaunched) {
            if (!ScreenViewImpl.oneActivityAlreadyLaunched) {
                ScreenViewImpl.oneActivityAlreadyLaunched = true
                launchActivityClass?.let { startActivity(Intent(this, it)) }
            }
            else {
                try {
                    screenViewImpl = ScreenViewImpl.getInstance(viewKey)!!
                } catch (ex: Exception) {
                    SKLog.e("onCreateSK -> No View for key $viewKey", ex)
                }
            }
        } else {
            ScreenViewImpl.oneActivityAlreadyLaunched = true
            val action = intent?.action
            val encodedPath = intent?.data?.encodedPath

            if (action != null && action == Intent.ACTION_VIEW && encodedPath != null) {
                val screenKey = ScreenViewImpl.onLink?.invoke(encodedPath, intent?.data?.encodedFragment)
                if (screenKey != null) {
                    screenViewImpl = ScreenViewImpl.getInstance(screenKey)
                } else {
                    SKLog.e("Please set ScreenViewImpl.onLink to treat $encodedPath link", IllegalStateException("ScreenViewImpl.onLink not set"))
                }

            } else {
                val initialGetter = ScreenViewImpl.getInitialViewImpl
                if (initialGetter != null) {
                    screenViewImpl = initialGetter()
                } else {
                    SKLog.e("Please set ScreenViewImpl.getInitialViewImpl", IllegalStateException("ScreenViewImpl.getInitialViewImpl not set"))
                }

            }
        }

        screenViewImpl?.let {
            setContentView(it.inflate(layoutInflater, this, null))
        } ?: finish()


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        screenViewImpl?.key?.let { outState.putLong(SK_EXTRA_VIEW_KEY, it) }

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
