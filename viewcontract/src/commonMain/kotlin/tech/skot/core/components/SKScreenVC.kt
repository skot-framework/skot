package tech.skot.core.components

interface SKScreenVC:SKComponentVC {
    var onBackPressed:(()->Unit)?
    var onResume:(()->Unit)?
    var onPause:(()->Unit)?
}