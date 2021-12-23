package tech.skot.core.components

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.skot.core.SKFeatureInitializer
import tech.skot.core.toSKUri
import tech.skot.view.extensions.updatePadding

abstract class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    companion object {
        private var oneActivityAlreadyLaunched = false
        var launchActivityClass: Class<out SKActivity>? = null
    }

    var screen: SKScreenView<*>? = null

    abstract val featureInitializer: SKFeatureInitializer


//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        intent?.data?.toSKUri()?.let { featureInitializer.onDeepLink?.invoke(it) }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            featureInitializer.initializeIfNeeded(intent?.data?.toSKUri())

            val viewKey = getKeyForThisActivity(savedInstanceState)
//            SKLog.d("@&@&@&@& ---- onCreate   viewKey : $viewKey")

            if (!oneActivityAlreadyLaunched && viewKey != -1L) {
                oneActivityAlreadyLaunched = true
                SKRootStackViewProxy.stateLD.value.state.screens.getOrNull(0)?.let {
                    startActivityForProxy(it)
                }
                finish()
            } else {
                val screenProxy =
                    if (viewKey != -1L) {
                        ScreensManager.getInstance(viewKey)
                    } else {
                        SKRootStackViewProxy.stateLD.value.state.screens.getOrNull(0)
                    } as? SKScreenViewProxy<*>

                oneActivityAlreadyLaunched = true


                screenProxy?.run {
                    screenKey = key
                    bindTo(this@SKActivity, null, layoutInflater)
                }
                    ?.run {
                        setContentView(this.view)
                        screen = this
                        linkToRootStack()
                        linkToCustoms()
                        if (resumedWithoutScreen) {
                            resumedWithoutScreen = false
                            onResume()
                        }
                    }
            }
        }
    }

    private var loadingInsetsCounter: Long = 0L

    fun setFullScreen(
        fullScreen: Boolean,
        lightStatusBar: Boolean,
        onWindowInsets: ((windowInsets: WindowInsetsCompat) -> Unit)? = null
    ) {

        screen?.view?.let {
            WindowInsetsControllerCompat(window, it).isAppearanceLightStatusBars = lightStatusBar
            WindowCompat.setDecorFitsSystemWindows(window, !fullScreen)
            loadingInsetsCounter++
            val loadedInsets = ViewCompat.getRootWindowInsets(it)
            if (loadedInsets != null) {
                it.updatePadding(
                    bottom = if (fullScreen) loadedInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    ).bottom else 0
                )
                onWindowInsets?.invoke(loadedInsets)
            } else {
                val loadingIndex = loadingInsetsCounter
                ViewCompat.setOnApplyWindowInsetsListener(it) { view, windowInsets ->
                    if (loadingInsetsCounter == loadingIndex) {
                        it.updatePadding(
                            bottom = if (fullScreen) windowInsets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                            ).bottom else 0
                        )
                    }
                    onWindowInsets?.invoke(windowInsets)
                    windowInsets
                }

            }
        }

    }


    private fun getKeyForThisActivity(savedInstanceState: Bundle?) =
        when {
            intent.hasExtra(ScreensManager.SK_EXTRA_VIEW_KEY) -> {
                intent.getLongExtra(
                    ScreensManager.SK_EXTRA_VIEW_KEY,
                    -1
                )
            }
            savedInstanceState?.containsKey(ScreensManager.SK_EXTRA_VIEW_KEY) == true -> {
                savedInstanceState.getLong(
                    ScreensManager.SK_EXTRA_VIEW_KEY,
                    -1
                )
            }
            else -> -1
        }


    private var resumedWithoutScreen = false

    override fun onResume() {
        super.onResume()
        screen.let {
            if (it == null) {
                resumedWithoutScreen = true
            } else {
                it.onResume()
            }
        }
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

        SKRootStackViewProxy.stateLD.observe(this) { (state, screenKeyNeedToOpenRoot) ->
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
            if (screenKeyNeedToOpenRoot != null && screenKeyNeedToOpenRoot == screenKey) {
                state.screens.firstOrNull()?.let { newScreen ->
                    val launchClass = launchActivityClass
                        ?: throw IllegalStateException("You have to set SKActivity.launchActivityClass to be allowed to change root Screen. New root screen will be loaded in this activity even if you have redefined getActivityClas method")
                    startActivity(Intent(this, launchClass).apply {
                        putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, newScreen.key)
                    })
                }
            }

        }

    }

    /**
     * override this method to register to messages or LiveData
     * main use is globally, for all activities, for platform specific work (sdk)
     */
    protected open fun linkToCustoms() {

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