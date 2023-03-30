package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.inputs.*
import tech.skot.core.components.presented.*
import tech.skot.core.view.Icon

class CoreViewInjectorMock : CoreViewInjector {
    override fun rootStack(): SKStackVC {
        return SKRootStackViewMock
    }

    override fun stack(): SKStackVC {
        return SKStackViewMock()
    }

    override fun alert(): SKAlertVC {
        return SKAlertViewMock()
    }

    override fun snackBar(): SKSnackBarVC {
        return SKSnackBarViewMock()
    }

    override fun bottomSheet(): SKBottomSheetVC {
        return SKBottomSheetViewMock()
    }

    override fun dialog(): SKDialogVC {
        return SKDialogViewMock()
    }

    override fun windowPopup(): SKWindowPopupVC {
        return SKWindowPopupViewMock()
    }

    override fun pager(
        screens: List<SKScreenVC>,
        onUserSwipeToPage: ((index:Int)->Unit)?,
        initialSelectedPageIndex: Int,
        swipable: Boolean
    ): SKPagerVC {
        return SKPagerViewMock(screens, onUserSwipeToPage, initialSelectedPageIndex, swipable)
    }

    override fun pagerWithTabs(
        pager: SKPagerVC,
        tabConfigs: List<SKPagerWithTabsVC.TabConfig>,
        visibility : SKPagerWithTabsVC.Visibility): SKPagerWithTabsVC {
        return SKPagerWithTabsViewMock(pager, tabConfigs, visibility)
    }


    override fun skList(
        layoutMode: SKListVC.LayoutMode,
        reverse: Boolean,
        animate: Boolean,
        animateItem: Boolean
    ): SKListVC {
        return SKListViewMock(layoutMode, reverse, animate, animateItem)
    }

    override fun skBox(itemsInitial: List<SKComponentVC>, hiddenInitial: Boolean?): SKBoxVC {
        return SKBoxViewMock(itemsInitial, hiddenInitial)
    }

    override fun webView(
        config: SKWebViewVC.Config,
        launchInitial: SKWebViewVC.Launch?
    ): SKWebViewVC {
        return SKWebViewViewMock(config, launchInitial)
    }

    override fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?): SKFrameVC {
        return SKFrameViewMock(screens, screenInitial)
    }

    override fun loader(): SKLoaderVC {
        return SKLoaderViewMock()
    }

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
    ): SKInputVC {
        return SKInputViewMock(
            onInputText,
            type,
            maxSize,
            onFocusChange,
            onDone,
            hintInitial,
            textInitial,
            errorInitial,
            hiddenInitial,
            enabledInitial,
            showPasswordInitial
        )
    }

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
    ): SKSimpleInputVC {
        return SKSimpleInputViewMock(
            onInputText,
            type,
            maxSize,
            onFocusChange,
            onDone,
            hintInitial,
            textInitial,
            errorInitial,
            hiddenInitial,
            enabledInitial,
            showPasswordInitial
        )
    }

    override fun combo(
        hint: String?,
        errorInitial: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial: Boolean,
        oldSchoolModeHint: Boolean
    ): SKComboVC {
        return SKComboViewMock(
            hint,
            errorInitial,
            onSelected,
            choicesInitial,
            selectedInitial,
            enabledInitial,
            hiddenInitial,
            dropDownDisplayedInitial,
            oldSchoolModeHint
        )
    }

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
    ): SKInputWithSuggestionsVC {
        return SKInputWithSuggestionsViewMock(
            hint,
            errorInitial,
            onSelected,
            choicesInitial,
            selectedInitial,
            enabledInitial,
            hiddenInitial,
            dropDownDisplayedInitial,
            onInputText,
            oldSchoolModeHint
        )
    }

    override fun button(
        onTapInitial: (() -> Unit)?,
        labelInitial: String?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        debounce:Long?
    ): SKButtonVC {
        return SKButtonViewMock(onTapInitial, labelInitial, enabledInitial, hiddenInitial, debounce)
    }

    override fun imageButton(
        onTapInitial: (() -> Unit)?,
        iconInitial: Icon,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        debounce:Long?
    ): SKImageButtonVC {
        return SKImageButtonViewMock(onTapInitial, iconInitial, enabledInitial, hiddenInitial, debounce)
    }
}