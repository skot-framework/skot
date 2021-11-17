//
//  SKComboView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKComboViewProxy:SKComponentViewProxy,ViewcontractSKComboVC {
    var choices: [ViewcontractSKComboVCChoice]
    
    var dropDownDisplayed: Bool
    
    var enabled: KotlinBoolean?
    
    var hidden: KotlinBoolean?
    
    var hint: String?
    
    var onSelected: ((Any?) -> Void)?
    
    var selected: ViewcontractSKComboVCChoice?
    
    init(hint: String?, onSelected: ((Any?) -> Void)?, choicesInitial: [ViewcontractSKComboVCChoice], selectedInitial: ViewcontractSKComboVCChoice?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?, dropDownDisplayedInitial: Bool) {
        self.hint = hint
        self.onSelected = onSelected
        self.choices = choicesInitial
        self.selected = selectedInitial
        self.enabled = enabledInitial
        self.hidden = hiddenInitial
        self.dropDownDisplayed = dropDownDisplayedInitial
    }
}
