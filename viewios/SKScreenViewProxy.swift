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
    var stack:SKStackViewProxy? = nil
    
    var name:String {
        get {
            "Ecran inconnu"
        }
    }

    
    init(visibilityListener:ViewcontractSKVisiblityListener) {
        self.visibilityListener = visibilityListener
    }
    
    
    
}
