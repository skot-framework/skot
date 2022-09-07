package tech.skot.core.components.presented

import kotlinx.coroutines.delay
import org.junit.Test
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.view.tests.SKTestView
import tech.skot.core.SKLog
import tech.skot.view.tests.testComponents

class TestSKAlertView: SKTestView() {

    @Test
    fun testAlert() {

        val alert = SKAlertViewProxy()

        val buttonShow = SKButtonViewProxy(
            labelInitial = "show",
            onTapInitial = {
                alert.state = SKAlertVC.Shown(
                    message = "1",
                    mainButton = SKAlertVC.Button(label ="re") {
                        alert.state = SKAlertVC.Shown(
                            message = "2",
                            mainButton = SKAlertVC.Button(label ="Ok"),
                            cancelable = true
                        )
                    },
                    cancelable = true
                )

            }
        )

        testComponents(
            alert,
            buttonShow
        ) {
            while (true) {
                delay(1000)
                SKLog.d("---alert state = ${alert.state}")
            }
        }
    }

}