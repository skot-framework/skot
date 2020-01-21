package tech.skot.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class SKActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_VIEW_KEY = "EXTRA_VIEW_KEY"

        inline fun <reified A : SKActivity> getIntent(context: Context, idView: Long) =
                Intent(context, A::class.java).apply {
                    putExtra(EXTRA_VIEW_KEY, idView)
                }
    }


    abstract fun getScreenViewForKey(key: Long): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val viewKey = intent.getLongExtra(EXTRA_VIEW_KEY, 0)
            setContentView(getScreenViewForKey(viewKey))

        } catch (ex: Exception) {
            finish()
        }
    }


    var onBackPressedAction: (() -> Unit)? = null

    override fun onBackPressed() {
        onBackPressedAction?.invoke() ?: super.onBackPressed()
    }
}