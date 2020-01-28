package tech.skot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.components.ScreenViewImpl.Companion.SK_EXTRA_VIEW_KEY
import tech.skot.core.SKLog


fun AppCompatActivity.onCreateSK(savedInstanceState: Bundle?) {
    val viewKey = intent.getLongExtra(SK_EXTRA_VIEW_KEY, 0)
    try {
        setContentView(ScreenViewImpl.getInstance(viewKey).inflate(layoutInflater, this, null))
    } catch (ex: Exception) {
        SKLog.e("onCreateSK -> No View for key $viewKey", ex)
        finish()
    }
}


abstract class SKActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateSK(savedInstanceState)
    }


    var onBackPressedAction: (() -> Unit)? = null

    //Pas de back par défaut, il faut faire attention avec ça
    override fun onBackPressed() {
        onBackPressedAction?.invoke()
    }
}
