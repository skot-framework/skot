package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.inputs.*
import tech.skot.core.components.presented.*
import tech.skot.core.view.Icon
import tech.skot.core.view.Style
import tech.skot.viewlegacy.R


class CoreViewInjectorImpl : CoreViewInjector {
    override fun rootStack() = SKRootStackViewProxy

    override fun stack() = SKStackViewProxy()

    override fun alert() = SKAlertViewProxy()

    override fun snackBar() = SKSnackBarViewProxy()

    override fun bottomSheet() = SKBottomSheetViewProxy()

    override fun dialog() = SKDialogViewProxy()

    override fun windowPopup() = SKWindowPopupViewProxy()

    override fun pager(
        screens: List<SKScreenVC>,
        onUserSwipeToPage:  ((index: Int) -> Unit)?,
        initialSelectedPageIndex: Int,
        swipable: Boolean
    ) = SKPagerViewProxy(
        initialScreens = screens as List<SKScreenViewProxy<*>>,
        onUserSwipeToPage = onUserSwipeToPage,
        initialSelectedPageIndex = initialSelectedPageIndex,
        swipable = swipable
    )


    override fun pagerWithTabs(
        pager: SKPagerVC,
        onUserTabClick: ((index: Int) -> Unit)?,
        tabConfigs: List<SKPagerWithTabsVC.TabConfig>,
        tabsVisibility: SKPagerWithTabsVC.Visibility
    ) =
        SKPagerWithTabsViewProxy(
            pager = pager as SKPagerViewProxy,
            onUserTabClick = onUserTabClick,
            initialTabConfigs = tabConfigs,
            initialTabsVisibility = tabsVisibility
        )


    override fun skList(
        layoutMode: SKListVC.LayoutMode,
        reverse: Boolean,
        animate: Boolean,
        animateItem: Boolean
    ) = SKListViewProxy(layoutMode, reverse, animate, animateItem)

    override fun skBox(itemsInitial: List<SKComponentVC>, hiddenInitial: Boolean?) =
        SKBoxViewProxy(itemsInitial as List<SKComponentViewProxy<*>>, hiddenInitial)

    override fun webView(config: SKWebViewVC.Config, launchInitial: SKWebViewVC.Launch?) =
        SKWebViewViewProxy(config, launchInitial)

    override fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?) = SKFrameViewProxy(
        screens = screens as Set<SKScreenViewProxy<*>>,
        screenInitial = screenInitial as SKScreenViewProxy<*>?
    )

    override fun loader() = SKLoaderViewProxy()

    override fun input(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusChange: ((hasFocus: Boolean) -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?,
        showPasswordInitial: Boolean?
    ) =
        SKInputViewProxy(
            maxSize,
            onDone,
            onFocusChange,
            onInputText,
            type,
            enabledInitial,
            errorInitial,
            hiddenInitial,
            hintInitial,
            textInitial,
            showPasswordInitial
        )

    override fun inputSimple(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusChange: ((hasFocus: Boolean) -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?,
        showPasswordInitial: Boolean?
    ) =
        SKSimpleInputViewProxy(
            maxSize,
            onDone,
            onFocusChange,
            onInputText,
            type,
            enabledInitial,
            errorInitial,
            hiddenInitial,
            hintInitial,
            textInitial,
            showPasswordInitial
        )

    override fun combo(
        hint: String?,
        error: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial: Boolean,
        oldSchoolModeHint: Boolean
    ) = SKComboViewProxy(
        hint = hint,
        errorInitial = error,
        onSelected = onSelected,
        choicesInitial = choicesInitial,
        selectedInitial = selectedInitial,
        enabledInitial = enabledInitial,
        hiddenInitial = hiddenInitial,
        dropDownDisplayedInitial = dropDownDisplayedInitial,
        oldSchoolModeHint = oldSchoolModeHint
    )

    override fun inputWithSuggestions(
        hint: String?,
        errorInitial: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial: Boolean,
        onInputText: (input: String?) -> Unit,
        oldSchoolModeHint: Boolean
    ) = SKInputWithSuggestionsViewProxy(
        hint = hint,
        errorInitial = errorInitial,
        onSelected = onSelected,
        choicesInitial = choicesInitial,
        selectedInitial = selectedInitial,
        enabledInitial = enabledInitial,
        hiddenInitial = hiddenInitial,
        dropDownDisplayedInitial = dropDownDisplayedInitial,
        onInputText = onInputText,
        oldSchoolModeHint = oldSchoolModeHint
    )

    override fun button(
        onTapInitial: (() -> Unit)?,
        labelInitial: String?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        debounce: Long?
    ) = SKButtonViewProxy(onTapInitial, labelInitial, enabledInitial, hiddenInitial, debounce)

    override fun imageButton(
        onTapInitial: (() -> Unit)?,
        iconInitial: Icon,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        debounce: Long?
    ) = SKImageButtonViewProxy(onTapInitial, iconInitial, enabledInitial, hiddenInitial, debounce)


}

val coreViewModule = module<BaseInjector> {
    single { CoreViewInjectorImpl() as CoreViewInjector }
    byName["skFullScreenDialogStyle"] = Style(R.style.sk_fullScreen_dialog)

}