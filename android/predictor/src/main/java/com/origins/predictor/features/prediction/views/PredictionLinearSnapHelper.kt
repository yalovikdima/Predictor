package com.origins.predictor.features.prediction.views

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import kotlin.math.abs
import kotlin.math.max

internal class PredictionLinearSnapHelper(
    private val isHomePicker: Boolean,
    private val itemPositionChangeListener: (Int, Boolean) -> Unit,
    private val onScrollListener: () -> Unit
) : LinearSnapHelper() {

    private var verticalHelper: OrientationHelper? = null
    private lateinit var recyclerView: RecyclerView
    private val scrollMsPerInch = 10f
    var isUserScroll = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            maybeNotifySnapPositionChange(recyclerView)
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (isUserScroll) {
                    isUserScroll = false
                    onScrollListener()
                }
                if (recyclerView.layoutManager != null) {
                    val view = findSnapView(recyclerView.layoutManager)
                    if (view != null) {
                        val out = calculateDistanceToFinalSnap(recyclerView.layoutManager!!, view)
                        recyclerView.smoothScrollBy(out[0], out[1])
                    }
                }
            }
        }
    }

    fun scrollTo(position: Int) {
        if (position >= 0) {
            recyclerView.post {
                recyclerView.layoutManager?.let { manager ->
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                    if (viewHolder != null) {
                        val distances =
                            calculateDistanceToFinalSnap(manager, viewHolder.itemView)
                        recyclerView.smoothScrollBy(distances[0], distances[1])
                    } else {
                        val smoothScroller = createScroller(manager)
                        smoothScroller?.let {
                            it.targetPosition = position
                            manager.startSmoothScroll(smoothScroller)
                        }
                    }
                }
            }
        }
    }

    override fun createScroller(layoutManager: RecyclerView.LayoutManager): SmoothScroller? {
        return if (layoutManager !is ScrollVectorProvider) {
            null
        } else object : LinearSmoothScroller(recyclerView.context) {
            override fun onTargetFound(
                targetView: View,
                state: RecyclerView.State,
                action: Action
            ) {
                val manager = recyclerView.layoutManager ?: return
                val snapDistances = calculateDistanceToFinalSnap(
                    manager,
                    targetView
                )
                val dx = snapDistances[0]
                val dy = snapDistances[1]
                val time = calculateTimeForDeceleration(max(abs(dx), abs(dy)))
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator)
                }
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return scrollMsPerInch / displayMetrics.densityDpi
            }
        }
    }

    private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
        val snapPosition = this.getSnapPosition(recyclerView.layoutManager)
        val adapter = recyclerView.adapter as PredictionScorePickerAdapter
        if (adapter.selectedPosition != snapPosition) {
            adapter.selectedPosition = snapPosition
            itemPositionChangeListener(snapPosition, isHomePicker)
            recyclerView.post {
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        layoutManager ?: return null
        return findCenterView(layoutManager, getVerticalHelper(layoutManager))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            this.recyclerView = recyclerView
            recyclerView.addOnScrollListener(scrollListener)
            recyclerView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_MOVE) {
                    isUserScroll = true
                }
               false
            }
        }
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    private fun findCenterView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }
        var closestChild: View? = null
        val center: Int = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        var absClosest = Integer.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter =
                (child!!.y + child.height / 2).toInt()
            val absDistance = Math.abs(childCenter - center)

            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun distanceToCenter(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View,
        helper: OrientationHelper
    ): Int {
        val childCenter =
            (targetView.y + targetView.height / 2).toInt()
        val containerCenter = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }
        return childCenter - containerCenter
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (verticalHelper == null || verticalHelper!!.layoutManager !== layoutManager) {
            verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return verticalHelper!!
    }
}

internal fun PredictionLinearSnapHelper.getSnapPosition(layoutManager: RecyclerView.LayoutManager?): Int {
    layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}