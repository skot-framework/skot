package tech.skot.core.components

import kotlinx.coroutines.delay
import org.junit.Test
import tech.skot.core.SKLog
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponent

class TestSKWebView : SKTestView() {

    @Test
    fun testRedirection() {
        val proxy = SKWebViewViewProxy(
            openUrlInitial = SKWebViewVC.OpenUrl(
                url = "https://www.casinomax.fr/actualites/casino-max-extra",
                onFinished = {
                    SKLog.d("@@@@@@@ finished !!!")
                }
            )
        )

        testComponent(proxy) {

            SKLog.d("@@@@  loading   !!")
            delay(4000)
            proxy.requestReload()
        }

    }
}