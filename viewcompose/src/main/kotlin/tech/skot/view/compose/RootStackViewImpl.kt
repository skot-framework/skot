package tech.skot.view.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import tech.skot.contract.view.RootStackView
import tech.skot.contract.view.ScreenView


class RootStackViewImpl : ComponentViewImpl(), RootStackView {

    val _screens: MutableState<List<ScreenView>> = mutableStateOf(emptyList())
    override var screens: List<ScreenView> by _screens


    @Composable
    override fun ui(modifier: Modifier) {
//        Crossfade(current = screens.lastOrNull()) {
//            (it as? ComponentViewImpl?)?.ui(modifier = modifier)
//        }
        if (screens.size % 2 != 0) {
            (screens.lastOrNull() as? ComponentViewImpl?)?.ui(modifier = modifier)
        } else {
            Box {
                (screens.lastOrNull() as? ComponentViewImpl?)?.ui(modifier = modifier)
            }
        }

    }


}



