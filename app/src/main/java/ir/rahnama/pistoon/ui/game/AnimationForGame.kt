package ir.rahnama.pistoon.ui.game

import android.view.animation.Transformation
import android.view.animation.TranslateAnimation


class AnimationForGame(
    fromXDelta: Float, toXDelta: Float, fromYDelta: Float,
    toYDelta: Float
) :
    TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta) {
    private var posAtPause: Long = 0
    private var mPaused = false


    override fun getTransformation(currentTime: Long, outTransformation: Transformation?): Boolean {
        if (mPaused && posAtPause == 0L) {
            posAtPause = currentTime - startTime
        }
        if (mPaused) startTime = currentTime - posAtPause
        return super.getTransformation(currentTime, outTransformation)
    }

    fun pause() {
        posAtPause = 0
        mPaused = true
    }

    fun resume() {
        mPaused = false
    }
}