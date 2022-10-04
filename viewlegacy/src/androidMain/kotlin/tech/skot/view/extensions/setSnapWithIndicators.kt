package tech.skot.view.extensions

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


fun RecyclerView.setSnapWithIndicators(
    indicatorsGroup: LinearLayout,
    indicatorSpacingDp: Int,
    unSelectedIndicator: Int,
    selectedIndicator: Int,
) {
    LinearSnapHelper().attachToRecyclerView(this)

    val marginDp = (resources.displayMetrics.density * indicatorSpacingDp / 2).roundToInt()
    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)
    params.leftMargin = marginDp
    params.rightMargin = marginDp


    val drawableNotSelected = ContextCompat.getDrawable(context, unSelectedIndicator)
    val drawableSelected = ContextCompat.getDrawable(context, selectedIndicator)

    var indicatorsViews: List<ImageView>? = null

    fun setIndicators() {
        indicatorsGroup.removeAllViews()
        indicatorsViews = adapter?.itemCount?.let {
            (1..it).map {
                ImageView(context).apply {
                    setImageDrawable(drawableNotSelected)
                    layoutParams = params
                }.also {
                    indicatorsGroup.addView(it)
                }

            }
        }
    }

    fun updateIndicators() {
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val selectedIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        indicatorsViews?.forEachIndexed { index, imageView ->
            imageView.setImageDrawable(if (index == selectedIndex) drawableSelected else drawableNotSelected)
        }
    }
    addOnChildAttachStateChangeListener(object :
        RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            setIndicators()
            updateIndicators()
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            setIndicators()
            updateIndicators()
        }

    })

    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            updateIndicators()
        }

    })
}
