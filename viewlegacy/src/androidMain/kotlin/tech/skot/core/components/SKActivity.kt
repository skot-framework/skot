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
//        intent?.data?.pathSegments?.let { featureInitializer.onDeepLink.invoke(it) }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {

            featureInitializer.initializeIfNeeded(intent?.data?.pathSegments)

            val viewKey = getKeyForThisActivity(savedInstanceState)
//            SKLog.d("@&@&@&@& ---- onCreate   viewKey : $viewKey")

            if (!oneActivityAlreadyLaunched && viewKey != -1L) {
                oneActivityAlreadyLaunched = true
                SKRootStackViewProxy.stateLD.value.screens.getOrNull(0)?.let {
                    startActivityForProxy(it)
                }
                finish()
            } else {
                val screenProxy =
                    if (viewKey != -1L) {
                        ScreensManager.getInstance(viewKey)
                    } else {
                        SKRootStackViewProxy.stateLD.value.screens.getOrNull(0)
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
                    }


            }


        }
    }

    fun setFullScreen(
        fullScreen: Boolean,
        lightStatusBar: Boolean,
        onWindowInsets: ((windowInsets: WindowInsetsCompat) -> Unit)? = null
    ) {
        screen?.view?.let {
            WindowInsetsControllerCompat(window, it).isAppearanceLightStatusBars = lightStatusBar
            WindowCompat.setDecorFitsSystemWindows(window, !fullScreen)
            val loadedInsets = ViewCompat.getRootWindowInsets(it)
            if (loadedInsets != null) {
                it.updatePadding(bottom = if (fullScreen) loadedInsets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom else 0)
                onWindowInsets?.invoke(loadedInsets)
            } else {
                ViewCompat.setOnApplyWindowInsetsListener(it) { view, windowInsets ->
                    it.updatePadding(
                        bottom = if (fullScreen) windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                        ).bottom else 0
                    )
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
            finish()
            val launchClass = launchActivityClass
                ?: throw IllegalStateException("You have to set SKActivity.launchActivityClass to be allowed to change root Screen. New root screen will be loaded in this activity even if you have redefined getActivityClas method")
            startActivity(Intent(this, launchClass).apply {
                putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, it.key)
            })
        }

        SKRootStackViewProxy.stateLD.observe(this) { state ->
            val thisScreenPosition = state.screens.indexOfFirst {
                it.key == screenKey
            }

//            SKLog.d("@&@&@&@&  ${this.hashCode()}  linkToRootStack  stateLD ${state.screens.map { "$it ${it.key}" }} key: ${screenKey} thisScreenPosition: $thisScreenPosition  ${screen?.let { it::class.simpleName }}")

            if (thisScreenPosition == -1) {
//                SKLog.d("@&@&@&@&  ${this.hashCode()}  linkToRootStack  stateLD ${state.screens.map { "$it ${it.key}" }} key: ${screenKey} thisScreenPosition: $thisScreenPosition will finish ${screen?.let { it::class.simpleName }}")
                finish()
                state.transition?.let { overridePendingTransition(it.enterAnim, it.exitAnim) }
            } else {
                if (state.screens.size > thisScreenPosition + 1) {
//                    SKLog.d("@&@&@&@&  ${this.hashCode()}  linkToRootStack  stateLD ${state.screens.map { "$it ${it.key}" }} key: ${screenKey} thisScreenPosition: $thisScreenPosition will start an activity ${state.screens.get(thisScreenPosition + 1)::class.simpleName}")
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