package tech.skot.core.components.presented

import android.R
import org.junit.Test
import tech.skot.core.components.inputs.SKButtonViewProxy
import tech.skot.core.view.Color
import tech.skot.core.view.Icon
import tech.skot.view.tests.SKTestView
import tech.skot.view.tests.testComponents

class TestSKSnackBarView : SKTestView() {

    @Test
    fun testSnack() {

        val snack = SKSnackBarViewProxy()

        val buttonShow = SKButtonViewProxy(
            labelInitial = "show simple",
            onTapInitial = {
                snack.state = SKSnackBarVC.Shown(
                    message = "message assez long l lksdjflkj slkdfj lkjsqdf lkj",
                    onTop = true,
                )

            }
        )

        val buttonShowLeftIcon = SKButtonViewProxy(
            labelInitial = "show left icon",
            onTapInitial = {
                snack.state = SKSnackBarVC.Shown(
                    message = "message assez long l lksdjflkj slkdfj lkjsqdf lkj",
                    onTop = true,
                    leftIcon = Icon(R.drawable.ic_delete)
                )

            }
        )

        val buttonShowRightIcon = SKButtonViewProxy(
            labelInitial = "show right icon",
            onTapInitial = {
                snack.state = SKSnackBarVC.Shown(
                    message = "message assez long l lksdjflkj slkdfj lkjsqdf lkj",
                    onTop = true,
                    rightIcon = Icon(R.drawable.ic_delete)
                )

            }
        )

        val buttonShowLeftAndRightIcon = SKButtonViewProxy(
            labelInitial = "show left and right icon",
            onTapInitial = {
                snack.state = SKSnackBarVC.Shown(
                    message = "message assez long l lksdjflkj slkdfj lkjsqdf lkj",
                    onTop = true,
                    rightIcon = Icon(R.drawable.ic_delete),
                    leftIcon = Icon(R.drawable.ic_dialog_info)
                )

            }
        )

        val buttonBackgroundColorAndLeftIcon = SKButtonViewProxy(
            labelInitial = "show left icon and backgroundcolor",
            onTapInitial = {
                snack.state = SKSnackBarVC.Shown(
                    message = "message assez long l lksdjflkj slkdfj lkjsqdf lkj",
                    onTop = true,
                    backgroundColor = Color(android.R.color.holo_red_dark),
                    leftIcon = Icon(R.drawable.ic_dialog_info)
                )

            }
        )

        val buttonHide = SKButtonViewProxy(
            labelInitial = "hide",
            onTapInitial = {
                snack.state = null

            }
        )



        testComponents(
            snack,
            buttonShow,
            buttonShowLeftIcon,
            buttonShowRightIcon,
            buttonShowLeftAndRightIcon,
            buttonBackgroundColorAndLeftIcon,
            buttonHide
        )


    }
}