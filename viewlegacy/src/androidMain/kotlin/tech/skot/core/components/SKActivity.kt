package tech.skot.core.components

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.skot.core.SKFeatureInitializer

abstract class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    companion object {
        var oneActivityAlreadyLaunched = false
    }

    private var screen: SKScreenView<*>? = null

    abstract val featureInitializer: SKFeatureInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            featureInitializer.initializeIfNeeded()

            val viewKey = getKeyForThisActivity(savedInstanceState)

            if (!oneActivityAlreadyLaunched && viewKey != -1L) {
                oneActivityAlreadyLaunched = true
                SKRootStackViewProxy.stateLD.value.screens.getOrNull(0)?.let {
                    startActivityForProxy(it)
                }
                finish()
            } else {
                oneActivityAlreadyLaunched = true
                (if (viewKey != -1L) {
                    ScreensManager.getInstance(viewKey)
                } else {
                    SKRootStackViewProxy.stateLD.value.screens.getOrNull(0)
                } as? SKScreenViewProxy<*>)?.run {
                    screenKey = key
                    bindTo(this@SKActivity, null, layoutInflater)
                }
                    ?.run {
                        setContentView(this.view)
                        screen = this
                        linkToRootStack()
                    }
            }
        }
    }

    private fun getKeyForThisActivity(savedInstanceState: Bundle?) =
        when {
            intent.hasExtra(ScreensManager.SK_EXTRA_VIEW_KEY) -> intent.getLongExtra(
                ScreensManager.SK_EXTRA_VIEW_KEY,
                -1
            )
            savedInstanceState?.containsKey(ScreensManager.SK_EXTRA_VIEW_KEY) == true -> savedInstanceState.getLong(
                ScreensManager.SK_EXTRA_VIEW_KEY,
                -1
            )
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

        SKRootStackViewProxy.stateLD.observe(this) { state ->
            val thisScreenPosition = state.screens.indexOfFirst {
                it.key == screenKey
            }

            if (thisScreenPosition == -1) {
                finish()
                state.transition?.let { overridePendingTransition(it.enterAnim, it.exitAnim) }
            } else {
                if (state.screens.size > thisScreenPosition + 1) {
                    startActivityForProxy(state.screens.get(thisScreenPosition + 1))
                    state.transition?.let { overridePendingTransition(it.enterAnim, it.exitAnim) }

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