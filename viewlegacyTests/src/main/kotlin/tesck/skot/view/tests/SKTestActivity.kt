package tesck.skot.view.tests

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import tech.skot.core.SKFeatureInitializer
import tech.skot.core.components.*
import tech.skot.core.view.Color
import tech.skot.core.view.Icon

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
    block: (CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit)? = null
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
            block?.invoke(this, scenario)
            delay(duration)
        }
    }
}

fun testComponent(
    componentViewProxy: SKComponentViewProxy<*>,
    duration: Long = 5 * 60 * 1000L,
    block: CoroutineScope.(scenario: ActivityScenario<SKTestActivity>) -> Unit
) {
    testScreen(SKTestScreenViewProxy(componentViewProxy), duration, block)
}
