//
//  SKScreenView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKScreenViewProxy: SKComponentViewProxy,ViewcontractSKScreenVC {
    var onBackPressed: (() -> Void)?
    
    let visibilityListener:ViewcontractSKVisiblityListener
    
    override func ui() -> AnyView {
        AnyView(Text("Screen"))
    }
    
    init(visibilityListener:ViewcontractSKVisiblityListener) {
        self.visibilityListener = visibilityListener
    }
}
