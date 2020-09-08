package com.bobko.wakeapptest

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.lifecycle.MutableLiveData

class GameController(var context: Context, var gameField: TableLayout, var player: ImageView)
{
    var levelField = mutableListOf<MutableList<View>>()
    private lateinit var flingAnimation: FlingAnimation
    lateinit var targetCell : View
    private var targetCellPositionInTableX = 0
    private var targetCellPositionInTableY = 0
    private var startPosition = intArrayOf(0,0)
    private var cellInLevel = 0
    var gameOver = MutableLiveData<Int>()
    fun loadLevel(level: IntArray)
    {
        gameField.removeAllViews()
        levelField.clear()
        for (i in 0..5) {
            var row = TableRow(context)
            var params = TableLayout.LayoutParams()
            var levelRow = mutableListOf<View>()

            params.width = TableRow.LayoutParams.MATCH_PARENT
            params.height = TableRow.LayoutParams.MATCH_PARENT
            params.weight = 1f
            row.layoutParams = params

            for (j in 0..4) {
                var cell = ImageView(context)
                var params = TableRow.LayoutParams()

                params.width = TableRow.LayoutParams.MATCH_PARENT
                params.height = TableRow.LayoutParams.MATCH_PARENT
                params.weight = 1f

                cell.layoutParams = params
                cell.scaleType = ImageView.ScaleType.FIT_XY
                cell.setImageResource(R.drawable.way_cell)
                if (level[j+(i*5)]==0)
                    cell.visibility = View.INVISIBLE
                else
                    cellInLevel++
                row.addView(cell)
                levelRow.add(cell)
            }

            levelField.add(levelRow)
            gameField.addView(row)
        }
        targetCell = levelField[5][2]
        targetCellPositionInTableX = 5
        targetCellPositionInTableY = 2
    }
    fun restart(level: IntArray)
    {
        levelField[5][2].getLocationOnScreen(startPosition)
        flingAnimation.cancel()
        player.x=startPosition[0].toFloat()+player.width/2
        player.y=startPosition[1].toFloat()
        loadLevel(level)
    }
    fun move(direction: Direction)
    {
        if (::flingAnimation.isInitialized)
            flingAnimation.cancel()
        flingAnimation = FlingAnimation(player, direction.getAxis())
        flingAnimation.setStartVelocity(direction.getDirection())
        flingAnimation.friction = 0.001f
        flingAnimation.setMaxValue(direction.getMaxValue(targetCell, player))
        flingAnimation.setMinValue(direction.getMinValue(targetCell, player))

        flingAnimation.addEndListener {_, canseld, _, _ -> flingAnimation?.let {if (!canseld) outOfCell(direction)}}
        flingAnimation.start()
    }
    fun setPlayerLocation()
    {
        levelField[5][2].getLocationOnScreen(startPosition)
        player.x=startPosition[0].toFloat()+player.width/2
        player.y=startPosition[1].toFloat()
    }
    private fun outOfCell(direction: Direction)
    {
        goneCell()
        cellInLevel--
        if (cellInLevel==0)
        {
            gameOver.value = R.drawable.ic_win
            return
        }
        if (isOutOfField(direction))
        {
            gameOver.value = R.drawable.ic_try_again
            return
        }
        if (checkNextCellEnable(direction))
        {
            gameOver.value = R.drawable.ic_try_again
            return
        }
        setNextCell(direction)
    }
    private fun setNextCell(direction: Direction)
    {
        if (direction.getAxis() == DynamicAnimation.X)
        {
            if (direction.getDirection()>0) {
                targetCellPositionInTableY++
            }
            else
                targetCellPositionInTableY--
        }
        else
            if (direction.getDirection()>0)
            {
                targetCellPositionInTableX++
            }
            else
                targetCellPositionInTableX--

        updateTargetCell(direction)

    }
    private fun updateTargetCell(direction: Direction)
    {
        targetCell = levelField[targetCellPositionInTableX][targetCellPositionInTableY]
        move(direction)
    }
    private fun isOutOfField(direction: Direction): Boolean
    {
        if (direction.getAxis() == DynamicAnimation.X && (targetCellPositionInTableY == 4 && direction.getDirection()>0))
        {
            return true
        }

        if (direction.getAxis() == DynamicAnimation.X && (targetCellPositionInTableY == 0 && direction.getDirection()<0))
        {
            return true
        }

        if (direction.getAxis() == DynamicAnimation.Y && (targetCellPositionInTableX == 5 && direction.getDirection()>0))
        {
            return true
        }

        if (direction.getAxis() == DynamicAnimation.Y && (targetCellPositionInTableX == 0 && direction.getDirection()<0))
        {
            return true
        }
        return false
    }
    private fun checkNextCellEnable(direction: Direction): Boolean
    {
        if (direction.getAxis() == DynamicAnimation.X && direction.getDirection()>0 && levelField[targetCellPositionInTableX][targetCellPositionInTableY+1].visibility == View.INVISIBLE)
        {
            return true
        }
        if (direction.getAxis() == DynamicAnimation.X && direction.getDirection()<0 && levelField[targetCellPositionInTableX][targetCellPositionInTableY-1].visibility == View.INVISIBLE)
        {
            return true
        }

        if (direction.getAxis() == DynamicAnimation.Y && direction.getDirection()>0 && levelField[targetCellPositionInTableX+1][targetCellPositionInTableY].visibility == View.INVISIBLE)
        {
            return true
        }
        if (direction.getAxis() == DynamicAnimation.Y && direction.getDirection()<0 && levelField[targetCellPositionInTableX-1][targetCellPositionInTableY].visibility == View.INVISIBLE)
        {
            return true
        }
        return false
    }
    private fun goneCell()
    {
        var cell = targetCell
        cell.animate()
            .alpha(0.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    cell.visibility = View.INVISIBLE
                }
            })

    }
}