//
//  SKRootStackViewProxy.swift
//  ua-burger-skot-swiftui
//
//  Created by Mathieu Scotet on 26/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKRootStackViewProxy:SKComponentViewProxy,ViewcontractSKStackVC, ObservableObject {
    @Published var state: ViewcontractSKStackVCState = ViewcontractSKStackVCState(screens: [], transition: nil)
 
    let viewLocator:ViewLocator = ViewLocator()
    
    let onDismissTopScreen: ()-> Void
    
    init(onDismissTopScreen: @escaping ()-> Void) {
        self.onDismissTopScreen = onDismissTopScreen
    }
    
    func ui() -> SKRootStackView
    {
        SKRootStackView(proxy:self)
    }
}

