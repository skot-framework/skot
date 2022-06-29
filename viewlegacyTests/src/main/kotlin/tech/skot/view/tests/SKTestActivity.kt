package tech.skot.view.tests

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import kotlinx.coroutines.*
import tech.skot.core.SKFeatureInitializer
import tech.skot.core.components.*
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.core.components.presented.SKDialogVC
import tech.skot.core.view.Color
import tech.skot.core.view.ColorRef
import tech.skot.core.view.Style

class SKTestActivity : SKActivity() {

    override val featureInitializer = object : SKFeatureInitializer(
        initialize = {},
        onDeepLink = null,
        start = {},
        resetToRoot = {
        }
    ) {}

}

fun testScreen(
    screenViewProxy: SKScreenViewProxy<*>,
    duration: Long = 5 * 60 * 1000L,
    block: (suspend CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
) {
    SKRootStackViewProxy.state = SKStackVC.State(
        screens = listOf(screenViewProxy)
    )
    launchActivity<SKTestActivity>(
        Intent(
            ApplicationProvider.getApplicationContext(),
            SKTestActivity::class.java
        )
    ).use { scenario ->
        runBlocking {
            withContext(Dispatchers.Main) {
                block?.invoke(this, scenario)
                delay(duration)
            }
        }
    }
}

fun testComponent(
    componentViewProxy: SKComponentViewProxy<*>,
    duration: Long = 5 * 60 * 1000L,
    vertical: Boolean = true,
    backgroundColor: Color? = null,
    block: (suspend CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
) {
    testScreen(
        SKTestScreenViewProxy(listOf(componentViewProxy), vertical, backgroundColor),
        duration,
        block
    )
}


fun testComponents(
    vararg componentsViewProxy: SKComponentViewProxy<*>,
    duration: Long = 5 * 60 * 1000L,
    vertical: Boolean = true,
    backgroundColor: ColorRef? = null,
    block: (suspend CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
) {
    testScreen(
        SKTestScreenViewProxy(
            componentsViewProxy.toList(), vertical, backgroundColor
        ), duration, block
    )
}


fun testDialog(
    screen: SKScreenViewProxy<*>,
    duration: Long = 5 * 60 * 1000L,
    vertical: Boolean = true,
    backgroundColor: ColorRef? = null,
    style: Style? = null,
    block: (suspend CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
) {
    val button = SKButtonViewProxy(labelInitial = "open dialog")
    val skScreenTest = SKTestScreenViewProxy(listOf(button), vertical, backgroundColor)
    button.onTap = {
        skScreenTest.dialog.state = SKDialogVC.Shown(
            screen = screen,
            cancelable = false,
            style = style
        )
    }

    testScreen(skScreenTest, duration, block)
}