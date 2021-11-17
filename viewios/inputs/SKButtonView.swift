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
    var enabled: KotlinBoolean?
    
    var hidden: KotlinBoolean?
    
    var label: String?
    
    var onTap: (() -> Void)?
    
    init(onTapInitial: (() -> Void)?, labelInitial: String?, enabledInitial: KotlinBoolean?, hiddenInitial: KotlinBoolean?) {
        self.onTap = onTapInitial
        self.label = labelInitial
        self.enabled = enabledInitial
        self.hidden = hiddenInitial
    }
    
    override func ui() -> AnyView {
        AnyView(SKButtonView(proxy: self))
    }
    
    
}

struct SKButtonView: View {
    
    @ObservedObject var proxy:SKButtonViewProxy
   
    var body: some View {
        Button(action:proxy.onTap ?? {}, label:{
            Text(proxy.label ?? "")
            })
        
    }
}
