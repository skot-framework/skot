//
//  SKPagerView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKPagerViewProxy:SKComponentViewProxy,ViewcontractSKPagerVC {
    let onSwipeToPage: ((KotlinInt) -> Void)?
    
    var screens: [ViewcontractSKScreenVC]
    
    var selectedPageIndex: Int32
    
    let swipable: Bool
    
    
    init(screens: [ViewcontractSKScreenVC], onSwipeToPage: ((KotlinInt) -> Void)?, initialSelectedPageIndex: Int32, swipable: Bool) {
        self.onSwipeToPage = onSwipeToPage
        self.swipable = swipable
        
        self.selectedPageIndex = initialSelectedPageIndex
        self.screens = screens
        
        
    }
    
}
