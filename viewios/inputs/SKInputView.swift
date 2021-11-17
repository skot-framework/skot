//
//  SKInputView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKInputViewProxy:SKComponentViewProxy,ViewcontractSKInputVC {
    func requestFocus() {
        
    }
    
    var enabled: KotlinBoolean?
    
    var error: String?
    
    var hidden: KotlinBoolean?
    
    var hint: String?
    
    let maxSize: KotlinInt?
    
    let onDone: ((String?) -> Void)?
    
    let onFocusLost: (() -> Void)?
    
    let onInputText: (String?) -> Void
    
    var text: String?
    
    let type: ViewcontractSKInputVCType?
    
    
    init(onInputText: @escaping (String?) -> Void, type: ViewcontractSKInputVCType?, maxSize: KotlinInt?, onFocusLost: (() -> Void)?, onDone: ((String?) -> Void)?, hintInitial: String?, textInitial: String?, errorInitial: String?, hiddenInitial: KotlinBoolean?, enabledInitial: KotlinBoolean?) {
        
        self.onInputText = onInputText
        self.type = type
        self.maxSize = maxSize
        self.onFocusLost = onFocusLost
        self.onDone = onDone
        
        self.hint = hintInitial
        self.text = textInitial
        self.error = errorInitial
        self.hidden = hiddenInitial
        self.enabled = enabledInitial
    }
}
