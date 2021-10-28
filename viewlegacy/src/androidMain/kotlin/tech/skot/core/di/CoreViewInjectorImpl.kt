package tech.skot.core.di

import tech.skot.core.view.Icon
import tech.skot.core.components.*
import tech.skot.core.components.inputs.*
import tech.skot.core.components.presented.*


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = SKRootStackViewProxy

    override fun stack() = SKStackViewProxy()

    override fun alert() = SKAlertViewProxy()

    override fun snackBar() = SKSnackBarViewProxy()

    override fun bottomSheet() = SKBottomSheetViewProxy()

    override fun windowPopup() = SKWindowPopupViewProxy()

    override fun pager(
        screens: List<SKScreenVC>,
        onSwipeToPage: ((index: Int) -> Unit)?,
        initialSelectedPageIndex: Int,
        swipable: Boolean
    ) = SKPagerViewProxy(
        initialScreens = screens as List<SKScreenViewProxy<*>>,
        onSwipeToPage = onSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )

    override fun pagerWithTabs(pager: SKPagerVC, labels: List<String>) =
        SKPagerWithTabsViewProxy(
            pager = pager as SKPagerViewProxy,
            initialLabels = labels
        )


    override fun skList(
        vertical: Boolean,
        reverse: Boolean,
        nbColumns: Int?,
        animate: Boolean,
        animateItem: Boolean
    ) = SKListViewProxy(vertical, reverse, nbColumns, animate, animateItem)

    override fun skBox(itemsInitial:List<SKComponentVC>, hiddenInitial: Boolean?) = SKBoxViewProxy(itemsInitial as List<SKComponentViewProxy<*>>, hiddenInitial)

    override fun webView(config: SKWebViewVC.Config, openUrlInitial: SKWebViewVC.OpenUrl?) =
        SKWebViewViewProxy(config, openUrlInitial)

    override fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?) = SKFrameViewProxy(
        screens = screens as Set<SKScreenViewProxy<*>>,
        screenInitial = screenInitial as SKScreenViewProxy<*>?
    )

    override fun loader() = SKLoaderViewProxy()

    override fun input(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusLost: (() -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?
    ) =
        SKInputViewProxy(
            maxSize,
            onDone,
            onFocusLost,
            onInputText,
            type,
            enabledInitial,
            errorInitial,
            hiddenInitial,
            hintInitial,
            textInitial
        )

    override fun inputSimple(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusLost: (() -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?
    ) =
        SKSimpleInputViewProxy(
            maxSize,
            onDone,
            onFocusLost,
            onInputText,
            type,
            enabledInitial,
            errorInitial,
            hiddenInitial,
            hintInitial,
            textInitial
        )

    override fun combo(
        hint: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial:Boolean
    ) = SKComboViewProxy(
        hint = hint,
        onSelected = onSelected,
        choicesInitial = choicesInitial,
        selectedInitial = selectedInitial,
        enabledInitial = enabledInitial,
        hiddenInitial = hiddenInitial,
        dropDownDisplayedInitial = dropDownDisplayedInitial
    )

    override fun inputWithSuggestions(
        hint: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial:Boolean,
        onInputText:(input:String?) -> Unit,
    ) = SKInputWithSuggestionsViewProxy(
        hint = hint,
        onSelected = onSelected,
        choicesInitial = choicesInitial,
        selectedInitial = selectedInitial,
        enabledInitial = enabledInitial,
        hiddenInitial = hiddenInitial,
        dropDownDisplayedInitial = dropDownDisplayedInitial,
        onInputText = onInputText,
    )

    override fun button(
        onTapInitial: (() -> Unit)?,
        labelInitial: String?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?
    ) = SKButtonViewProxy(onTapInitial, labelInitial, enabledInitial, hiddenInitial)

    override fun imageButton(
        onTapInitial: (() -> Unit)?,
        iconInitial: Icon,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?
    ) = SKImageButtonViewProxy(onTapInitial, iconInitial, enabledInitial, hiddenInitial)


}

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
}