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
 
    
    override func ui() -> AnyView
    {
        AnyView(SKStackView(proxy:self))
    }
}

struct SKStackView: View {
    
    @ObservedObject var proxy:SKStackViewProxy
    
 
    var body: some View {
        //let nbScreens = proxy.state.screens.count
        //Text(String(format:"stack %d",nbScreens))
        if (proxy.state.screens.isEmpty) {
            Text("Empty stack")
        }
        else {
            (proxy.state.screens.last as! SKScreenViewProxy).ui()
        }
    }
    
}
