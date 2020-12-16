package tech.skot.view.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onDispose
import androidx.compose.ui.Modifier
import tech.skot.contract.view.ComponentView


abstract class ComponentViewImpl : ComponentView {

    @Composable
    abstract fun ui(modifier: Modifier = Modifier)

    override fun onRemove() {}

    protected val savedStates:MutableMap<String,Any> = mutableMapOf()

    @Composable
    fun skScrollState(key:String = "SCROLL"): ScrollState {
        val scrollState = rememberScrollState(initial = (savedStates.getOrDefault(key, 0f) as? Float) ?: 0f)
        onDispose(callback = {
            savedStates[key] = scrollState.value
        })
        return scrollState
    }
}