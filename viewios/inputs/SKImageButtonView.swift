//
//  SKImageButtonView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKImageButtonViewProxy:SKComponentViewProxy,ViewcontractSKImageButtonVC {
    var enabled: KotlinBoolean?
    
    var hidden: KotlinBoolean?
    
    var icon: ViewcontractIcon
    
    var onTap: (() -> Void)?
    
    init(onTapInitial: (() -> Void)?, iconInitial: ViewcontractIcon, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?) {
        self.onTap = onTapInitial
        self.icon = iconInitial
        self.enabled = enabledInitial
        self.hidden = hiddenInitial
    }
}
