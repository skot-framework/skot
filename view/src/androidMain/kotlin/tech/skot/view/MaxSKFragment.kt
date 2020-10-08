package tech.skot.view

abstract class MaxSKFragment: SKFragment() {
    var onResumeLambda: (() -> Unit)? = null
    override fun onResume() {
        super.onResume()
        onResumeLambda?.invoke()
    }

    var onPauseLambda: (() -> Unit)? = null
    override fun onPause() {
        super.onPause()
        onPauseLambda?.invoke()
    }
}