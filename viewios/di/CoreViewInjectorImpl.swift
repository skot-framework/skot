//
//  CoreViewInjectorImpl.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class CoreViewInjectorImpl: ViewcontractCoreViewInjector {
    func alert() -> ViewcontractSKAlertVC {
        return SKAlertViewProxy()
    }
    
    func bottomSheet() -> ViewcontractSKBottomSheetVC {
        return SKBottomSheetViewProxy()
    }
    
    func button(onTapInitial: (() -> Void)?, labelInitial: String?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?) -> ViewcontractSKButtonVC {
        return SKButtonViewProxy(onTapInitial: onTapInitial, labelInitial: labelInitial, enabledInitial: enabledInitial, hiddenInitial: hiddenInitial)
    }
    
    func combo(hint: String?, onSelected: ((Any?) -> Void)?, choicesInitial: [ViewcontractSKComboVCChoice], selectedInitial: ViewcontractSKComboVCChoice?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?, dropDownDisplayedInitial: Bool) -> ViewcontractSKComboVC {
        return SKComboViewProxy(hint: hint, onSelected: onSelected, choicesInitial: choicesInitial, selectedInitial: selectedInitial, enabledInitial: enabledInitial, hiddenInitial: hiddenInitial, dropDownDisplayedInitial: dropDownDisplayedInitial)
    }
    
    func frame(screens: Set<AnyHashable>, screenInitial: ViewcontractSKScreenVC?) -> ViewcontractSKFrameVC {
        return SKFrameViewProxy(screens: screens, screenInitial: screenInitial)
    }
    
    func imageButton(onTapInitial: (() -> Void)?, iconInitial: ViewcontractIcon, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?) -> ViewcontractSKImageButtonVC {
        return SKImageButtonViewProxy(onTapInitial: onTapInitial, iconInitial: iconInitial, enabledInitial: enabledInitial, hiddenInitial: hiddenInitial)
    }
    
    func input(onInputText: @escaping (String?) -> Void, type: ViewcontractSKInputVCType?, maxSize: KotlinInt?, onFocusLost: (() -> Void)?, onDone: ((String?) -> Void)?, hintInitial: String?, textInitial: String?, errorInitial: String?, hiddenInitial: KotlinBoolean?, enabledInitial: KotlinBoolean?) -> ViewcontractSKInputVC {
        return SKInputViewProxy(onInputText: onInputText, type: type, maxSize: maxSize, onFocusLost: onFocusLost, onDone: onDone, hintInitial: hintInitial, textInitial: textInitial, errorInitial: errorInitial, hiddenInitial: hiddenInitial, enabledInitial: enabledInitial)
    }
    
    func inputSimple(onInputText: @escaping (String?) -> Void, type: ViewcontractSKInputVCType?, maxSize: KotlinInt?, onFocusLost: (() -> Void)?, onDone: ((String?) -> Void)?, hintInitial: String?, textInitial: String?, errorInitial: String?, hiddenInitial: KotlinBoolean?, enabledInitial: KotlinBoolean?) -> ViewcontractSKSimpleInputVC {
        return SKSimpleInputViewProxy(onInputText: onInputText, type: type, maxSize: maxSize, onFocusLost: onFocusLost, onDone: onDone, hintInitial: hintInitial, textInitial: textInitial, errorInitial: errorInitial, hiddenInitial: hiddenInitial, enabledInitial: enabledInitial)
    }
    
    func inputWithSuggestions(hint: String?, onSelected: ((Any?) -> Void)?, choicesInitial: [ViewcontractSKComboVCChoice], selectedInitial: ViewcontractSKComboVCChoice?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?, dropDownDisplayedInitial: Bool, onInputText: @escaping (String?) -> Void) -> ViewcontractSKInputWithSuggestionsVC {
        return SKInputWithSuggestionsViewProxy(hint: hint, onSelected: onSelected, choicesInitial: choicesInitial, selectedInitial: selectedInitial, enabledInitial: enabledInitial, hiddenInitial: hiddenInitial, dropDownDisplayedInitial: dropDownDisplayedInitial, onInputText: onInputText)
    }
    
    func loader() -> ViewcontractSKLoaderVC {
        return SKLoaderViewProxy()
    }
    
    func pager(screens: [ViewcontractSKScreenVC], onSwipeToPage: ((KotlinInt) -> Void)?, initialSelectedPageIndex: Int32, swipable: Bool) -> ViewcontractSKPagerVC {
        return SKPagerViewProxy(screens: screens, onSwipeToPage: onSwipeToPage, initialSelectedPageIndex: initialSelectedPageIndex, swipable: swipable)
    }
    
    func pagerWithTabs(pager: ViewcontractSKPagerVC, labels: [String]) -> ViewcontractSKPagerWithTabsVC {
        return SKPagerWithTabsViewProxy(pager: pager as! SKPagerViewProxy, labels: labels)
    }
    
    func rootStack() -> ViewcontractSKStackVC {
        return SKStackViewProxy()
    }
    
    func skBox(itemsInitial: [ViewcontractSKComponentVC], hiddenInitial: KotlinBoolean?) -> ViewcontractSKBoxVC {
        return SKBoxViewProxy(itemsInitial: itemsInitial, hiddenInitial: hiddenInitial)
    }
    
    func skList(vertical: Bool, reverse: Bool, nbColumns: KotlinInt?, animate: Bool, animateItem: Bool) -> ViewcontractSKListVC {
        return SKListViewProxy(vertical: vertical, reverse: reverse, nbColumns: nbColumns, animate: animate, animateItem: animateItem)
    }
    
    func snackBar() -> ViewcontractSKSnackBarVC {
        return SKSnackBarViewProxy()
    }
    
    func stack() -> ViewcontractSKStackVC {
        return SKStackViewProxy()
    }
    
    func webView(config: ViewcontractSKWebViewVCConfig, openUrlInitial: ViewcontractSKWebViewVCOpenUrl?) -> ViewcontractSKWebViewVC {
        return SKWebViewProxy(config: config, openUrlInitial: openUrlInitial)
    }
    
    func windowPopup() -> ViewcontractSKWindowPopupVC {
        return SKWindowPopupViewProxy()
    }
    
    
}
