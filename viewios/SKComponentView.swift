//
//  ComponentView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKComponentViewProxy: ViewcontractSKComponentVC {
    
    let id = UUID()
    
    func closeKeyboard() {
        
    }
    
    
    
    func displayErrorMessage(message: String) {
        print("Erreur message --------- " + message)
    }
    
    func onRemove() {
        
    }
    
    var style: ViewcontractStyle?
    
    func ui()-> AnyView {
        AnyView(Text("Vide"))
    }
    
}
