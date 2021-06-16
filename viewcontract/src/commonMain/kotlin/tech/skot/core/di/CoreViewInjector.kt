package tech.skot.core.di

import tech.skot.core.components.*
import tech.skot.core.components.inputs.SKButtonVC
import tech.skot.core.components.inputs.SKComboVC
import tech.skot.core.components.inputs.SKInputVC
import tech.skot.core.components.inputs.SKInputWithSuggestionsVC
import tech.skot.core.components.presented.*

interface CoreViewInjector {
    fun rootStack(): SKStackVC
    fun stack(): SKStackVC
    fun alert(): SKAlertVC
    fun snackBar(): SKSnackBarVC
    fun bottomSheet(): SKBottomSheetVC
    fun pager(screens:List<SKScreenVC>, onSwipeToPage:((index:Int)->Unit)?, initialSelectedPageIndex:Int, swipable:Boolean): SKPagerVC
    fun pagerWithTabs(pager:SKPagerVC, labels:List<String>): SKPagerWithTabsVC
    fun skList(vertical:Boolean, reverse:Boolean, nbColumns:Int?, animate:Boolean, animateItem:Boolean): SKListVC
    fun webView(config: SKWebViewVC.Config, openUrlInitial: SKWebViewVC.OpenUrl?): SKWebViewVC
    fun frame(screens: Set<SKScreenVC>, screenInitial: SKScreenVC?): SKFrameVC
    fun loader(): SKLoaderVC

    fun input(
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
    ) : SKInputVC

    fun combo(
        hint: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        dropDownDisplayedInitial:Boolean
    ): SKComboVC

    fun inputWithSuggestions(
        hint: String?,
        onSelected: ((choice: Any?) -> Unit)?,
        choicesInitial: List<SKComboVC.Choice>,
        selectedInitial: SKComboVC.Choice?,
        enabledInitial: Boolean?,
        dropDownDisplayedInitial:Boolean,
        onInputText:(input:String?) -> Unit,
        textInitial: String?,
    ): SKInputWithSuggestionsVC


    fun button(
        onTapInitial: (() -> Unit)?,
        labelInitial: String?,
        enabledInitial: Boolean?,
        hiddenInitial: Boolean?
    ):SKButtonVC
}
