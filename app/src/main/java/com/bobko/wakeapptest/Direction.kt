package com.bobko.wakeapptest

import android.util.Log
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import kotlin.math.atan2
import kotlin.math.sqrt

class Direction(startX: Float, startY: Float, x:Float, y: Float) {
    private var angle = getAngle(startX,startY,x,y)

    fun getDirection(): Float
    {
        if (inRange(angle, 0F, 45F) || inRange(angle, 315F, 360F) || inRange(angle, 225F, 315F))
            return 100f
        else
            return -100f
    }

    fun getAxis(): DynamicAnimation.ViewProperty
    {
        if (inRange(angle, 45F, 135F) || inRange(angle, 225F, 315F))
            return DynamicAnimation.Y
        else return DynamicAnimation.X
    }

    fun getAngle(startX: Float, startY: Float, x:Float, y: Float): Double
    {
        var rad = atan2(startY - y, x - startX) + Math.PI;
        return (rad * 180 / Math.PI + 180) % 360;
    }

    private fun inRange(
        angle: Double,
        init: Float,
        end: Float
    ): Boolean {
        return angle >= init && angle < end
    }

    fun getMaxValue(targetView: View, player: View): Float
    {
        var posTarget = IntArray(2)

        targetView.getLocationOnScreen(posTarget)

        if (getAxis()==DynamicAnimation.X)
        {
            return (posTarget[0] + targetView.width - player.width/2).toFloat()
        }
        else{
            return (posTarget[1] + targetView.height-player.height).toFloat()
        }
    }
    fun getMinValue(targetView: View, player: View): Float
    {
        var posTarget = IntArray(2)

        targetView.getLocationOnScreen(posTarget)

        if (getAxis()==DynamicAnimation.X)
        {
            return (posTarget[0] - player.width/2).toFloat()
        }
        else{
            return (posTarget[1] - player.height*1.5).toFloat()
        }
    }
}