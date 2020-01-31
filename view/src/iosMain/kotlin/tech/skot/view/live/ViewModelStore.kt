package tech.skot.view.live

import platform.UIKit.UIViewController
import kotlin.reflect.KClass

interface WithViewModelOwner {
    val viewModelStore: ViewModelStore
}

abstract class BaseViewModel

class ViewModelStore {
    val vms = mutableMapOf<KClass<*>, BaseViewModel>()

    private var isActive = false
    fun isActive() = isActive

    interface LifeCycleObserver {
        fun activated()
        fun willDesactivate()
        fun willDestroy()
    }

    val observers = mutableListOf<LifeCycleObserver>()

    //called on ViewDidAppear
    fun onActivate() {
        isActive = true
        observers.forEach { it.activated() }
    }

    //called on ViewWillDisappear
    fun onDesactivate() {
        isActive = false
        observers.forEach { it.willDesactivate() }
    }

    fun destroy() {
        isActive = false
        observers.forEach { it.willDestroy() }
    }

    fun getOwnersList(uiViewController: UIViewController): List<ViewModelStore> {
        return listOf(this) + (uiViewController.parentViewController?.let {
            (it as? WithViewModelOwner)?.viewModelStore?.getOwnersList(it) ?: emptyList()
        } ?: emptyList())

    }

}