package tech.skot.core.components

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.skot.core.SKFeatureInitializer
import tech.skot.core.toSKUri
import tech.skot.view.SKPermissionAndroid
import tech.skot.view.SKPermissionsRequestResultAndroid
import tech.skot.view.extensions.updatePadding

abstract class SKActivity : AppCompatActivity() {

    var screenKey: Long? = null

    companion object {
        private var oneActivityAlreadyLaunched = false
        var launchActivityClass: Class<out SKActivity>? = null
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (launchActivityClass?.isInstance(this) == true) {
            featureInitializer.resetToRoot()
            intent?.data?.toSKUri()?.let { featureInitializer.onDeepLink?.invoke(it, false) }
        }
    }

    var screen: SKScreenView<*>? = null

    abstract val featureInitializer: SKFeatureInitializer

    var statusBarColor: Int = 0
    var themeWindowLightStatusBar: Boolean = true
    var requestedStatusBarColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.windowLightStatusBar, typedValue, true)
            themeWindowLightStatusBar = typedValue.data != 0
        }

        lifecycleScope.launch {

            featureInitializer.initializeIfNeeded(intent?.data?.toSKUri(), intent?.action)

            val viewKey = getKeyForThisActivity(savedInstanceState)

            if (SKRootStackViewProxy.stateLD.value.screens.isEmpty()) {
                featureInitializer.start(intent?.action)
            }

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
                            ?: SKRootStackViewProxy.stateLD.value.screens.getOrNull(0)
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
                        linkToCustoms()
                        if (resumedWithoutScreen) {
                            resumedWithoutScreen = false
                            onResume()
                        }
                    }
            }
        }

        statusBarColor = window.statusBarColor
    }

    private var loadingInsetsCounter: Long = 0L


    fun setFullScreen(
        fullScreen: Boolean,
        lightStatusBar: Boolean?,
        onWindowInsets: ((windowInsets: WindowInsetsCompat) -> Unit)? = null,
    ) {

        screen?.view?.let {
            it.post {
                WindowInsetsControllerCompat(window, it).isAppearanceLightStatusBars =
                    lightStatusBar ?: themeWindowLightStatusBar
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
                    it.requestApplyInsets()
                } else {
                    val loadingIndex = loadingInsetsCounter
                    ViewCompat.setOnApplyWindowInsetsListener(it) { _, windowInsets ->
                        if (loadingInsetsCounter == loadingIndex) {
                            ViewCompat.setOnApplyWindowInsetsListener(it, null)
                            it.updatePadding(
                                bottom = if (fullScreen) windowInsets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                                ).bottom else 0
                            )
                        }
                        onWindowInsets?.invoke(windowInsets)
                        it.requestApplyInsets()
                        windowInsets
                    }
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

    private var finishedAsked = false

    private fun linkToRootStack() {

        SKRootStackViewProxy.stateLD.observe(this) { state ->
            if (!finishedAsked) {
                val thisScreenPosition = state.screens.indexOfFirst {
                    it.key == screenKey
                }
                val isTaskRootBeforeFinish = isTaskRoot
                if (thisScreenPosition == -1) {
                    finishedAsked = true
                    finish()
                    state.transition?.let { overridePendingTransition(it.enterAnim, it.exitAnim) }
                } else {
                    if (state.screens.size > thisScreenPosition + 1) {
                        startActivityForProxy(state.screens.get(thisScreenPosition + 1))
                        state.transition?.let {
                            overridePendingTransition(
                                it.enterAnim,
                                it.exitAnim
                            )
                        }
                    }
                }

                if (finishedAsked && isTaskRootBeforeFinish) {
                    state.screens.firstOrNull()?.let { newScreen ->
                        val launchClass = launchActivityClass
                            ?: throw IllegalStateException("You have to set SKActivity.launchActivityClass to be allowed to change root Screen. New root screen will be loaded in this activity even if you have redefined getActivityClas method")
                        startActivity(Intent(this@SKActivity, launchClass).apply {
                            flags = flags.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            putExtra(ScreensManager.SK_EXTRA_VIEW_KEY, newScreen.key)
                        })
                    }
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        ScreensManager.permissionsResults.post(
            SKPermissionsRequestResultAndroid(
                requestCode = requestCode,
                grantedPermissions = permissions.filterIndexed { index, _ ->
                    grantResults[index] == PackageManager.PERMISSION_GRANTED
                }.map { SKPermissionAndroid(it) }
            )

        )
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}