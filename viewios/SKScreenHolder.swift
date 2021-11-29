//
//  SKScreenHolder.swift
//  ua-burger-skot-swiftui
//
//  Created by Mathieu Scotet on 26/11/2021.
//

import Foundation
import SwiftUI

struct SKScreenHolder<Content:View>: View {
 @EnvironmentObject var rootStack: SKRootStackViewProxy
    
    let proxy:SKScreenViewProxy
    let content:() -> Content
    
    
    var body: some View {
        
        
        //RootStack
        
        let indexOfThisInRootStack:Int = rootStack.state.screens.firstIndex { screen in
            return (screen as! SKScreenViewProxy).id == proxy.id
        } ?? -1
        let hasSheet = indexOfThisInRootStack > -1 && indexOfThisInRootStack < rootStack.state.screens.count - 1
        
        let isSheetPresented = Binding<Bool>(
            get: { hasSheet },
            set :{ value in rootStack.onDismissTopScreen() })
        
        //other stack
        let indexOfThisInStack:Int = proxy.stack?.state.screens.firstIndex { screen in
            return (screen as! SKScreenViewProxy).id == proxy.id
        } ?? -1
        let hasNext = indexOfThisInStack > -1 && indexOfThisInStack < (proxy.stack?.state.screens.count ?? 0) - 1
        
        let hasOnTop = Binding<Bool>(
            get: { hasNext },
            set :{ value in proxy.stack?.onDismissTopScreen() })
        
        return
                content()
                    .sheet(isPresented: isSheetPresented) {
                        rootStack.viewLocator.getView(proxy: rootStack.state.screens[indexOfThisInRootStack + 1] as! SKScreenViewProxy)
                    }
                    .background(
                        Group {
                            if (hasNext) {
                                NavigationLink(destination:
                                                rootStack.viewLocator.getView(proxy: proxy.stack?.state.screens[indexOfThisInStack + 1] as! SKScreenViewProxy)
                                               , isActive: hasOnTop, label: {EmptyView()})
                            }
                        }
                    )
                
                    
        
        

    }
    
    
    
}
