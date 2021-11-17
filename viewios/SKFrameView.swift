//
//  SKFrameView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKFrameViewProxy:SKComponentViewProxy,ViewcontractSKFrameVC {
    var screen: ViewcontractSKScreenVC?
    
    let screens: Set<AnyHashable>
    
    init(screens: Set<AnyHashable>, screenInitial: ViewcontractSKScreenVC?) {
        self.screens = screens
        self.screen = screenInitial
    }
    
}
