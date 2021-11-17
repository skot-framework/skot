//
//  SKInputWithSuggestions.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKInputWithSuggestionsViewProxy:SKComponentViewProxy,ViewcontractSKInputWithSuggestionsVC {
    func requestFocus() {
        
    }
    
    let onInputText: (String?) -> Void
    
    var enabled: KotlinBoolean?
    
    var hidden: KotlinBoolean?
    
    let hint: String?
    
    var choices: [ViewcontractSKComboVCChoice]
    
    var dropDownDisplayed: Bool
    
    let onSelected: ((Any?) -> Void)?
    
    var selected: ViewcontractSKComboVCChoice?
    
    
    
    
    init(hint: String?, onSelected: ((Any?) -> Void)?, choicesInitial: [ViewcontractSKComboVCChoice], selectedInitial: ViewcontractSKComboVCChoice?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?, dropDownDisplayedInitial: Bool, onInputText: @escaping (String?) -> Void) {
        
        self.onInputText = onInputText
        self.hint = hint
        self.onSelected = onSelected
        
        self.choices = choicesInitial
        self.selected = selectedInitial
        self.enabled = enabledInitial
        self.hidden = hiddenInitial
        self.dropDownDisplayed = dropDownDisplayedInitial
    }
}
