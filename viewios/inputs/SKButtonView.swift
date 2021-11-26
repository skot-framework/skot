//
//  SKButtonView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKButtonViewProxy:SKComponentViewProxy,ViewcontractSKButtonVC, ObservableObject {
    @Published var enabled: KotlinBoolean?
    
    @Published var hidden: KotlinBoolean?
    
    @Published var label: String?
    
    @Published var onTap: (() -> Void)?
    
    init(onTapInitial: (() -> Void)?, labelInitial: String?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?) {
        self.onTap = onTapInitial
        self.label = labelInitial
        self.enabled = enabledInitial
        self.hidden = hiddenInitial
    }
    
    func ui() -> SKButtonView {
        SKButtonView(proxy: self)
    }
    
    
}

struct SKButtonView: View {
    
    @ObservedObject var proxy:SKButtonViewProxy
   
    var body: some View {
        if (!(proxy.hidden?.boolValue == false)) {
            Button(action:proxy.onTap ?? {}, label:{
                Text(proxy.label ?? "??")
            }).disabled(proxy.enabled?.boolValue == false)
        }
        
        
    }
}
