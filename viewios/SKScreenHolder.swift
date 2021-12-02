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
    
    @State var isShowingNext = false {
        didSet {
            print("did set \(isShowingNext)")
        }
    }
    
    var body: some View {
        
        
        //RootStack
//
//        let indexOfThisInRootStack:Int = rootStack.state.screens.firstIndex { screen in
//            return (screen as! SKScreenViewProxy).id == proxy.id
//        } ?? -1
//        let hasSheet = indexOfThisInRootStack > -1 && indexOfThisInRootStack < rootStack.state.screens.count - 1
//
//        let isSheetPresented = Binding<Bool>(
//            get: { hasSheet },
//            set :{ value in rootStack.onDismissTopScreen() })
//
//        //other stack
//        let indexOfThisInStack:Int = proxy.stack?.state.screens.firstIndex { screen in
//            return (screen as! SKScreenViewProxy).id == proxy.id
//        } ?? -1
//        let hasNext = indexOfThisInStack > -1 && indexOfThisInStack < (proxy.stack?.state.screens.count ?? 0) - 1
//
//
//        let hasOnTop = Binding<Bool>(
//            get: { hasNext },
//            set :{ value in proxy.stack?.onDismissTopScreen() })
//
//        print("--------------name: \(proxy.name) id: \(proxy.id) ")
//        print("indexOfThisInStack  \(indexOfThisInStack)  hasNext \(hasNext)  hasOnTop \(hasOnTop.wrappedValue) proxy.stack.count \(proxy.stack?.state.screens.count)")
        
        return
                content()
            .background(
                Group {

                        NavigationLink(
                            destination:
                                ViewLocator().getView(proxy: proxy.nextProxy)
                                ,
                            isActive: $isShowingNext) {
                                EmptyView()
                            }

                }
            ).onChange(of: proxy.hasNext, perform: { newHasNext in
                print("----ProxyHAsNext did change !")
                isShowingNext = proxy.hasNext
            })
        
        
//                    .sheet(isPresented: isSheetPresented) {
//                        rootStack.viewLocator.getView(proxy: rootStack.state.screens[indexOfThisInRootStack + 1] as! SKScreenViewProxy)
//                    }
//                    .background(
//                        Group {
//                                    NavigationLink(destination:
//                                                    rootStack.viewLocator.getViewWithHolder(proxy: proxy.stack?.state.screens[indexOfThisInStack + 1] as! SKScreenViewProxy, stack: proxy.stack)
//                                                   , isActive: hasOnTop, label: {EmptyView()})

//                                }
//                        }
//                    )
                
                    
        
        

    }
    
    
    
}
