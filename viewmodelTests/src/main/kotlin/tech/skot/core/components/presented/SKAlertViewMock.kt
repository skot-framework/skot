package tech.skot.core.components.presented

import tech.skot.core.components.SKComponentViewMock
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SKAlertViewMock : SKComponentViewMock(), SKAlertVC {
    override var state: SKAlertVC.Shown? = null
}

fun SKAlertVC.assertCloseOnTapMainButton(rule:String? = null) {
    val errorPrefix = rule?.let { "$it -> " } ?: ""
    userTapMainButton()
    assertTrue("${errorPrefix}l'alerte doit se fermer au tap sur le bouton principal") {
        state == null
    }
}

fun SKAlertVC.assertCloseOnTapSecondButton(rule:String? = null) {
    val errorPrefix = rule?.let { "$it -> " } ?: ""
    userTapSecondaryButton()
    assertTrue("${errorPrefix}l'alerte doit se fermer au tap sur le bouton principal") {
        state == null
    }
}

fun SKAlertVC.assertNotDisplayed(rule: String? = null) {
    val errorPrefix = rule?.let { "$it -> " } ?: ""
    assertTrue("${errorPrefix}une alerte ne doit pas être affichée") {
        state == null
    }
}

fun SKAlertVC.assertDisplayedWith(
    rule: String? = null,
    title: String? = null,
    message: String? = null,
    mainButtonLabel:String? = null,
    secondaryButtonLabel:String? = null
) {
    val errorPrefix = rule?.let { "$it -> " } ?: ""
    assertTrue("${errorPrefix}une alerte doit être affichée") {
        state != null
    }
    title?.let {
        assertEquals(
            message = "${errorPrefix}le titre de l'alerte doit être correct",
            expected = it,
            actual = state?.title
        )
    }
    message?.let {
        assertEquals(
            message = "${errorPrefix}le message de l'alerte doit être correct",
            expected = it,
            actual = state?.message
        )
    }

    mainButtonLabel?.let {
        assertTrue("${errorPrefix}l'alerte doit avoir un bouton principal") {
            state?.mainButton != null
        }
        assertEquals(
            message = "${errorPrefix}le label du bouton principal doit être correct",
            expected = it,
            actual = state?.mainButton?.label
        )
    }

    secondaryButtonLabel?.let {
        assertTrue("${errorPrefix}l'alerte doit avoir un bouton secondaire") {
            state?.secondaryButton != null
        }
        assertEquals(
            message = "${errorPrefix}le label du bouton secondaire doit être correct",
            expected = it,
            actual = state?.secondaryButton?.label
        )
    }

}

fun SKAlertVC.userTapMainButton() {
    state?.mainButton?.action?.invoke() ?: throw Exception("Pas de main bouton")
}

fun SKAlertVC.userTapSecondaryButton() {
    state?.secondaryButton?.action?.invoke() ?: throw Exception("Pas de second bouton")
}