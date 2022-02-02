package tech.skot.view.tests

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import kotlinx.coroutines.*
import tech.skot.core.SKFeatureInitializer
import tech.skot.core.components.*

class SKTestActivity : SKActivity() {

    override val featureInitializer = object : SKFeatureInitializer(
        initialize = {},
        onDeepLink = null,
        start = {}
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
    vertical:Boolean = true,
    block: (suspend CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
) {
    testScreen(SKTestScreenViewProxy(componentViewProxy, vertical), duration, block)
}
