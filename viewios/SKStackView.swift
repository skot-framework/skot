//
//  SKStackView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKStackViewProxy:SKComponentViewProxy,ViewcontractSKStackVC, ObservableObject {
        
    
    var state: ViewcontractSKStackVCState = ViewcontractSKStackVCState(screens: [], transition: nil) {
        didSet {
            if (state.screens.count > 1) {
                for index in 0...(state.screens.count-2) {
                    let proxyScreen = state.screens[index] as! SKScreenViewProxy
                    proxyScreen.stack = self
                    proxyScreen.nextProxy = state.screens[index + 1] as? SKScreenViewProxy
                    proxyScreen.hasNext = true
                }
            }
            if let lastScreen = (state.screens.last  as? SKScreenViewProxy){
                lastScreen.hasNext = false
                lastScreen.nextProxy = nil
            }
        }
    }
    
    let onDismissTopScreen: ()-> Void
    
    init(onDismissTopScreen: @escaping ()-> Void) {
        self.onDismissTopScreen = onDismissTopScreen
        super.init()
    }
    
    func ui() -> SKStackView
    {
        SKStackView(proxy:self)
    }
    
}

struct SKStackView: View {
    
    @ObservedObject var proxy:SKStackViewProxy
    let viewLocator = ViewLocator()
 
    var body: some View {
        if (proxy.state.screens.isEmpty) {
            Text("Empty stack")
        }
        else {
            NavigationView {
                viewLocator.getView(proxy: proxy.state.screens.first as! SKScreenViewProxy)
            }
        }
    }
    
}
