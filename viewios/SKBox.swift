//
//  SKBox.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKBoxViewProxy:SKComponentViewProxy,ViewcontractSKBoxVC {
    var hidden: KotlinBoolean?
    
    var items: [ViewcontractSKComponentVC]
        
    init(itemsInitial: [ViewcontractSKComponentVC], hiddenInitial: KotlinBoolean?) {
        self.hidden = hiddenInitial
        self.items = itemsInitial
        
    }
}
