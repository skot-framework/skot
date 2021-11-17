//
//  SKWebView.swift
//  iosApp
//
//  Created by Mathieu Scotet on 03/11/2021.
//

import Foundation
import shared

class SKWebViewProxy:SKComponentViewProxy, ViewcontractSKWebViewVC {
    let config: ViewcontractSKWebViewVCConfig
    
    var goBack: ViewcontractSKWebViewVCBackRequest? = nil
    
    var openUrl: ViewcontractSKWebViewVCOpenUrl?
    
    init(config: ViewcontractSKWebViewVCConfig, openUrlInitial: ViewcontractSKWebViewVCOpenUrl?) {
        self.config = config
        self.openUrl = openUrlInitial
    }
    
}
