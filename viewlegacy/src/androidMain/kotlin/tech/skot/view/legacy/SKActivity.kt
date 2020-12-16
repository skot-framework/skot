package tech.skot.view.legacy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewKey = intent.getLongExtra(ScreensManager.SK_EXTRA_VIEW_KEY, -1)
        (if (viewKey != -1L) {
            ScreensManager.getInstance(viewKey)
        } else {
            RootStackViewProxy.screens.getOrNull(0)
        } as? ScreenViewProxy<*>)?.run {
            screenKey = key
            inflate(layoutInflater, this@SKActivity, null)
        }
                .run {
                    setContentView(this)
                    linkToRootStack()
                }

    }


    private fun linkToRootStack() {

        RootStackViewProxy.setRootScreenMessage.observe(this) {
            startActivity(Intent(this, SKActivity::class.java).apply {
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
                    startActivity(Intent(this, SKActivity::class.java).apply {
                        putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, it.get(thisScreenPosition + 1).key)
                    })
                }
            }
        }
    }

    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        onBackPressedAction?.invoke()
    }

}