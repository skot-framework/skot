//
//  SKRootStackView.swift
//  ua-burger-skot-swiftui
//
//  Created by Mathieu Scotet on 26/11/2021.
//

import Foundation
import SwiftUI

struct SKRootStackView: View {
    
    @ObservedObject var proxy:SKRootStackViewProxy
    let viewLocator = ViewLocator()
 
    var body: some View {
        //let nbScreens = proxy.state.screens.count
        //Text(String(format:"stack %d",nbScreens))
        if (proxy.state.screens.isEmpty) {
            Text("Empty stack")
        }
        else {
            let rootProxy = proxy.state.screens[0] as! SKScreenViewProxy
            SKScreenHolder(proxy: rootProxy) {
                viewLocator.getView(proxy: rootProxy)
            }
            
            //ViewLocator().getView(proxy: proxy.state.screens.first as! SKScreenViewProxy)
            
        }
    }
    
/*
    @ViewBuilder func getScreen(index:Int) -> some View {
        let screen = ViewLocator().getView(proxy: proxy.state.screens[index] as! SKScreenViewProxy)
           // screen.sheet(isPre: <#T##Binding<Bool>#>, onDismiss: <#T##(() -> Void)?##(() -> Void)?##() -> Void#>, content: <#T##() -> View#>)
            //print("Il y a un écran au dessus")
        screen
    }*/
    
}
