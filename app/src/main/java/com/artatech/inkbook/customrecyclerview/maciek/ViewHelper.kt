package pl.inkcompat.Inkbookrecycler

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.view.ViewTreeObserver

fun View.increaseHitRect(dx : Int, dy : Int) {
    post {
        val rect = Rect()
        getHitRect(rect)
        rect.inset(-dx, -dy)
        (parent as View).setTouchDelegate(TouchDelegate(rect, this))
    }
}

fun <T : View> T.onSizeAvailable(function: (width : Int, height : Int) -> Unit) {
    if (height == 0 || width == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(width, height)
            }
        })
    else function(width, height)
}

fun View.isVisible() : Boolean {
    return visibility == View.VISIBLE
}

fun View.setVisible(visible : Boolean) {
    visibility = (if (visible) View.VISIBLE else View.INVISIBLE)
}