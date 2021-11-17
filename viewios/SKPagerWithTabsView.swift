//
//  PagerWithTabs.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKPagerWithTabsViewProxy:SKComponentViewProxy,ViewcontractSKPagerWithTabsVC {
    
    var labels: [String]
    
    let pager: ViewcontractSKPagerVC
    let pagerView: SKPagerViewProxy
    
    init(pager: SKPagerViewProxy, labels: [String]) {
        self.pager = pager
        self.pagerView = pager
        
        self.labels = labels
    }
}
