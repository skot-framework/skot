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
    @Published var state: ViewcontractSKStackVCState = ViewcontractSKStackVCState(screens: [], transition: nil)
 
    let onDismissTopScreen: ()-> Void
    
    init(onDismissTopScreen: @escaping ()-> Void) {
        self.onDismissTopScreen = onDismissTopScreen
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
        //let nbScreens = proxy.state.screens.count
        //Text(String(format:"stack %d",nbScreens))
        if (proxy.state.screens.isEmpty) {
            Text("Empty stack")
        }
        else {
            NavigationView {
                viewLocator.getView(proxy: proxy.state.screens.first as! SKScreenViewProxy, stack: proxy)
            }
            //ViewLocator().getView(proxy: proxy.state.screens.last as! SKScreenViewProxy)
        }
    }
    
}
