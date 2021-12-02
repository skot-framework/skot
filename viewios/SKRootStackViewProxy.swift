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
    
    let viewLocator:ViewLocator = ViewLocator()
        
    
    var state: ViewcontractSKStackVCState = ViewcontractSKStackVCState(screens: [], transition: nil) {
        didSet {
            stateProxy = state
            print("----- did set state of SKRootStackViewProxy !! \(state.screens.count)")
            if (state.screens.count > 1) {
                for index in 0...(state.screens.count-2) {
                    let proxyScreen = state.screens[index] as! SKScreenViewProxy
                    proxyScreen.modalProxy = state.screens[index + 1] as? SKScreenViewProxy
                    proxyScreen.hasModal = true
                }
            }
            if let lastScreen = (state.screens.last  as? SKScreenViewProxy){
                lastScreen.hasModal = false
                lastScreen.modalProxy = nil
            }
        }
    }
    
    @Published var stateProxy:ViewcontractSKStackVCState = ViewcontractSKStackVCState(screens: [], transition: nil)
    
    let onDismissTopScreen: ()-> Void
    
    init(onDismissTopScreen: @escaping ()-> Void) {
        self.onDismissTopScreen = onDismissTopScreen
        super.init()
    }
    
    
    
    func ui() -> SKRootStackView
    {
        SKRootStackView(proxy:self)
    }
}

