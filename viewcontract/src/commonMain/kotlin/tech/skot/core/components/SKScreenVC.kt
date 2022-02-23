package tech.skot.core.components

import tech.skot.core.view.Color

interface SKScreenVC:SKComponentVC {
    var onBackPressed:(()->Unit)?
    var statusBarColor:Color?
}