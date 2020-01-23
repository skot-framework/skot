package tech.skot.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import tech.skot.components.ScreenViewImpl
import tech.skot.view.SKActivity.Companion.EXTRA_VIEW_KEY


interface SKActivity {
    fun getScreenViewForKey(key: Long):View
    var onBackPressedAction: (() -> Unit)?

    companion object {

        const val EXTRA_VIEW_KEY = "EXTRA_VIEW_KEY"

        inline fun <reified A : SKActivity> getIntent(context: Context, idView: Long) =
                Intent(context, A::class.java).apply {
                    putExtra(EXTRA_VIEW_KEY, idView)
                }
    }

    val activity:AppCompatActivity

    fun onCreateSK(savedInstanceState: Bundle?) {
        try {
            val viewKey = activity.intent.getLongExtra(EXTRA_VIEW_KEY, 0)
            activity.setContentView(getScreenViewForKey(viewKey))

        } catch (ex: Exception) {
            activity.finish()
        }
    }
}

abstract class SKActivityImpl : AppCompatActivity(),SKActivity {

    override fun getScreenViewForKey(key: Long) =
            ScreenViewImpl.getInstance<ScreenViewImpl<*,SKActivity,SKFragment>>(key)
                    .inflate(layoutInflater, Container(this, null))

    override val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateSK(savedInstanceState)
//        try {
//            val viewKey = intent.getLongExtra(EXTRA_VIEW_KEY, 0)
//            setContentView(getScreenViewForKey(viewKey))
//
//        } catch (ex: Exception) {
//            finish()
//        }
    }


    override var onBackPressedAction: (() -> Unit)? = null

    override fun onBackPressed() {
        onBackPressedAction?.invoke() ?: super.onBackPressed()
    }
}
