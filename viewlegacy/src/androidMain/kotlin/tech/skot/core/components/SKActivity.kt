package tech.skot.core.components

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

open class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    companion object {
        var oneActivityAlreadyLaunched = false
    }

    private var screen:SKScreenView<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewKey = getKeyForThisActivity(savedInstanceState)

        if (!oneActivityAlreadyLaunched && viewKey != -1L) {
            oneActivityAlreadyLaunched = true
            SKRootStackViewProxy.screensLD.value.getOrNull(0)?.let { startActivityForProxy(it) }
            finish()
        } else {
            oneActivityAlreadyLaunched = true
            (if (viewKey != -1L) {
                ScreensManager.getInstance(viewKey)
            } else {
                SKRootStackViewProxy.screens.getOrNull(0)
            } as? SKScreenViewProxy<*>)?.run {
                screenKey = key
                bindTo(this@SKActivity, null, layoutInflater)
            }
                    ?.run {
                        setContentView(this.view)
                        screen = this
                        linkToRootStack()
                    }
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    private fun getKeyForThisActivity(savedInstanceState: Bundle?) =
            when {
                intent.hasExtra(ScreensManager.SK_EXTRA_VIEW_KEY) -> intent.getLongExtra(ScreensManager.SK_EXTRA_VIEW_KEY, -1)
                savedInstanceState?.containsKey(ScreensManager.SK_EXTRA_VIEW_KEY) == true -> savedInstanceState.getLong(ScreensManager.SK_EXTRA_VIEW_KEY, -1)
                else -> -1
            }

    override fun onResume() {
        super.onResume()
        screen?.onResume()
    }

    override fun onPause() {
        super.onPause()
        screen?.onPause()
    }

    override fun onDestroy() {
        screenKey?.let { ScreensManager.getInstance(it) }?.apply {
            saveState()
        }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        screenKey?.let { outState.putLong(ScreensManager.SK_EXTRA_VIEW_KEY, it) }
    }

    private fun linkToRootStack() {

        SKRootStackViewProxy.setRootScreenMessage.observe(this) {
            startActivity(Intent(this, it.getActivityClass()).apply {
                putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, it.key)
            })
        }

        SKRootStackViewProxy.screensLD.observe(this) {
            val thisScreenPosition = it.indexOfFirst {
                it.key == screenKey
            }

            if (thisScreenPosition == -1) {
                finish()
            } else {
                if (it.size > thisScreenPosition + 1) {
                    startActivityForProxy(it.get(thisScreenPosition + 1))
                }
            }
        }

    }

    private fun startActivityForProxy(proxy: SKScreenViewProxy<*>) {
        startActivity(Intent(this, proxy.getActivityClass()).apply {
            putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, proxy.key)
        })
    }


    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        ScreensManager.backPressed.post(Unit)
    }

}