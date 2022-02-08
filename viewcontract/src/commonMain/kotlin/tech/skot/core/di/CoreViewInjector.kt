package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.inputs.*
import tech.skot.core.components.presented.*
import tech.skot.core.view.Icon

interface CoreViewInjector {
    fun rootStack(): SKStackVC
    fun stack(): SKStackVC
    fun alert(): SKAlertVC
    fun snackBar(): SKSnackBarVC
    fun bottomSheet(): SKBottomSheetVC
    fun dialog(): SKDialogVC
    fun windowPopup() : SKWindowPopupVC
    fun pager(screens:List<SKScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int, swipable:Boolean): SKPagerVC
    fun pagerWithTabs(pager:SKPagerVC, labels:List<String>): SKPagerWithTabsVC
    fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?, animate:Boolean, animateItem:Boolean): SKListVC
    fun skBox(itemsInitial:List<SKComponentVC>, hiddenInitial: Boolean?): SKBoxVC
    fun webView(config: SKWebViewVC.Config, openUrlInitial: SKWebViewVC.OpenUrl?): SKWebViewVC
    fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?): SKFrameVC
    fun loader(): SKLoaderVC

    fun input(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusChange: ((hasFocus:Boolean) -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?,
        showPasswordInitial: Boolean?
    ) : SKInputVC

    fun inputSimple(
        onInputText: (newText: String?) -> Unit,
        type: SKInputVC.Type?,
        maxSize: Int?,
        onFocusChange: ((hasFocus:Boolean) -> Unit)?,
        onDone: ((text: String?) -> Unit)?,
        hintInitial: String?,
        textInitial: String?,
        errorInitial: String?,
        hiddenInitial: Boolean?,
        enabledInitial: Boolean?,
        showPasswordInitial: Boolean?
    ) : SKSimpleInputVC

    fun combo(
        hint: String?,
        errorInitial: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial:Boolean,
        oldSchoolModeHint: Boolean
    ): SKComboVC

    fun inputWithSuggestions(
        hint: String?,
        errorInitial: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?,
        dropDownDisplayedInitial:Boolean,
        onInputText:(input:String?) -> Unit,
        oldSchoolModeHint: Boolean
    ): SKInputWithSuggestionsVC


    fun button(
        onTapInitial: (() -> Unit)?,
        labelInitial: String?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?
    ):SKButtonVC

    fun imageButton(
        onTapInitial: (() -> Unit)?,
        iconInitial: Icon,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?
    ):SKImageButtonVC
}
