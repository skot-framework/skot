package tech.skot.core.components

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import tech.skot.core.SKLog

open class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewKey = intent.getLongExtra(ScreensManager.SK_EXTRA_VIEW_KEY, -1)
        (if (viewKey != -1L) {
            ScreensManager.getInstance(viewKey)
        } else {
            SKLog.d("-------- SKActivity rootStack.screens ${RootStackViewProxy.screens}")
            RootStackViewProxy.screens.getOrNull(0)
        } as? ScreenViewProxy<*>)?.run {
            SKLog.d("-------- SKActivity screenProxy $this")
            screenKey = key
            bindTo(this@SKActivity, null, layoutInflater)
        }
                .run {
                    setContentView(this)
                    linkToRootStack()
                }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun onDestroy() {
        SKLog.d("SK Activity onDestroy $screenKey")
        screenKey?.let { ScreensManager.getInstance(it) }?.apply {
            SKLog.d("SK Activity onDestroy $screenKey will save State ${this::class.simpleName}")
            saveState()
        }
        super.onDestroy()
    }

    private fun linkToRootStack() {

        RootStackViewProxy.setRootScreenMessage.observe(this) {
            startActivity(Intent(this, it.getActivityClass()).apply {
                putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, it.key)
            })
        }

        RootStackViewProxy.screensLD.observe(this) {
            val thisScreenPosition = it.indexOfFirst {
                it.key == screenKey
            }

            if (thisScreenPosition == -1) {
                finish()
            } else {
                if (it.size > thisScreenPosition + 1) {
                    val onTopScreen = it.get(thisScreenPosition + 1)
                    startActivity(Intent(this, onTopScreen.getActivityClass()).apply {
                        putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, onTopScreen.key)
                    })
                }
            }
        }

//        (rootStack.view as RootStackViewProxy).bottomSheet.bindTo(this, null, layoutInflater, Unit)
    }

//    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
//        onBackPressedAction?.invoke()
        ScreensManager.backPressed.post(Unit)
    }

}