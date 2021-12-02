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

    @Published var hasNext:Bool = false
    var nextProxy:SKScreenViewProxy? = nil
    
    @Published var hasModal:Bool = false
    var modalProxy:SKScreenViewProxy? = nil
    

    /*var indexOfThisInStack:Int{
        get {
            stack?.state.screens.firstIndex { screen in
                return (screen as! SKScreenViewProxy).id == id
            } ?? -1
        }
    }*/
    

//    //other stack
//    let indexOfThisInStack:Int = proxy.stack?.state.screens.firstIndex { screen in
//        return (screen as! SKScreenViewProxy).id == proxy.id
//    } ?? -1
//    let hasNext = indexOfThisInStack > -1 && indexOfThisInStack < (proxy.stack?.state.screens.count ?? 0) - 1
    
    
    
    
    init(visibilityListener:ViewcontractSKVisiblityListener) {
        self.visibilityListener = visibilityListener
    }
    
    
    
}
