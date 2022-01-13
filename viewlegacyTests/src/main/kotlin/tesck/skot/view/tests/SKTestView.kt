package tesck.skot.view.tests

import android.content.Context
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import tech.skot.core.components.SKBoxViewProxy
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.components.SKListViewProxy
import tech.skot.core.components.SKVisiblityListener
import tech.skot.core.components.inputs.*

abstract class SKTestView {

    companion object {
        fun dummyVisiblityListener(): SKVisiblityListener = object : SKVisiblityListener {
            override fun onResume() {
            }

            override fun onPause() {
            }
        }
    }

    fun toast(message: String): () -> Unit = {
        Toast.makeText(
            ApplicationProvider.getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }


    fun dummyButton(toast: String? = null, label:String? = null, ): SKButtonViewProxy = SKButtonViewProxy(
        onTapInitial = toast?.let { toast(it) },
        labelInitial = label,
        enabledInitial = null,
        hiddenInitial = null
    )

    fun dummyCombo(selected:String = "choice"):SKComboViewProxy {
        val choice = SKComboVC.Choice(selected)
        return SKComboViewProxy(
            hint = null,
            onSelected = null,
            choicesInitial = listOf(choice),
            selectedInitial =  choice,
            enabledInitial = null,
            hiddenInitial = null,
            dropDownDisplayedInitial = false
        )
    }

    fun dummyBox(vararg component:SKComponentViewProxy<*>) = SKBoxViewProxy(
        itemsInitial = component.asList(),
        hiddenInitial = false,
    )

    fun dummyList(vararg component:SKComponentViewProxy<*>) = SKListViewProxy(
        vertical = true,
        reverse = false,
        nbColumns = null,
        animate = true,
        animateItem = false
    ).apply { setItems(*component) }

    fun dummyInput(text:String? = null, hint:String? = null) = SKInputViewProxy(
        maxSize = null,
        onDone = null,
        onFocusChange = null,
        onInputText = {},
        type = null,
        enabledInitial = null,
        errorInitial = null,
        hiddenInitial = null,
        hintInitial = hint,
        textInitial = text,
        showPasswordInitial = null
    )

    fun dummySimpleInput(text:String? = null, hint:String? = null) = SKSimpleInputViewProxy(
        maxSize = null,
        onDone = null,
        onFocusChange = null,
        onInputText = {},
        type = null,
        enabledInitial = null,
        errorInitial = null,
        hiddenInitial = null,
        hintInitial = hint,
        textInitial = text,
        showPasswordInitial = null
    )


    fun dummyShortText() =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla commodo semper nulla, cursus placerat justo suscipit et."

    fun dummyMediumText() =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla commodo semper nulla, cursus placerat justo suscipit et. Proin pharetra, massa eu laoreet aliquet, libero ligula accumsan purus, quis dapibus sapien arcu vel ante. Aenean ac volutpat velit. Nunc dolor mi, porta nec lobortis ac, tristique luctus justo. Aliquam volutpat pretium mauris, blandit imperdiet diam aliquam at. Donec quis pharetra orci, ut consequat sem. Quisque eleifend lacus purus, vitae maximus augue dignissim et. Integer commodo tincidunt enim, eu condimentum velit sollicitudin at. Quisque eleifend neque luctus neque gravida fringilla. Morbi vel sapien mollis, lacinia libero efficitur, placerat nunc. Nullam ut cursus odio, at rutrum nulla. Morbi sed elit facilisis, dapibus eros eu, scelerisque lectus. Ut non justo id metus vestibulum ornare. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos."

    fun dummyLargeText() = dummyMediumText() + dummyMediumText() + dummyMediumText()

    fun SKListViewProxy.setItems(vararg component:SKComponentViewProxy<*>) {
        this.items = component.map {
            Triple(it, it, null)
        }
    }

    fun string(res:Int) = ApplicationProvider.getApplicationContext<Context>().getString(res)

}