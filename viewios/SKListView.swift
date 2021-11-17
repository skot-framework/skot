//
//  SKListView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared
import SwiftUI

class SKListViewProxy:SKComponentViewProxy,ViewcontractSKListVC, ObservableObject {
   
    class SKListProxyItem: Any {
        let component:SKComponentViewProxy
        let id:Any
        let onSwipe:(() -> Void)?
        
        init(component:SKComponentViewProxy, id:Any, onSwipe:(() -> Void)?) {
            self.component = component
            self.id = id
            self.onSwipe = onSwipe
        }
    }
    
    @Published var itemsProxy: [SKListProxyItem]
    
    
    var items: [ViewcontractSKListVCItem] {
        didSet {
            itemsProxy = items.map({ item in
                SKListProxyItem(component: item.component as! SKComponentViewProxy, id: item.id, onSwipe: item.onSwipe)
            })
        }
    }
    
    func scrollToPosition(position: Int32) {
        
    }
    
    init(vertical: Bool, reverse: Bool, nbColumns: KotlinInt?, animate: Bool, animateItem: Bool) {
        self.items = []
        self.itemsProxy = []
        super.init()
    }
    
    
    override func ui() -> AnyView
    {
        AnyView(SKListView(proxy:self))
    }
   
}

struct SKListView:View {
    
    @ObservedObject var proxy:SKListViewProxy
    
    
    var body: some View {
        ScrollView {
            LazyVStack(alignment: HorizontalAlignment.leading) {
                ForEach(proxy.itemsProxy, id:\.component.id) { item in
                    item.component.ui()
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight:.infinity)
    }
}
