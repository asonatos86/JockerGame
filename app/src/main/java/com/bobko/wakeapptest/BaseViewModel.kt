package com.bobko.wakeapptest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_main.*

class BaseViewModel : ViewModel()
{
    var retry = MutableLiveData<Boolean>()
    lateinit var gameOver: MutableLiveData<Int>
    private lateinit var gameController: GameController

    fun setGameController(gameController: GameController)
    {
        this.gameController = gameController
        gameOver = gameController.gameOver
    }
    fun retry()
    {
        retry.value = true
    }
    fun loadLevel(level: IntArray)
    {
        gameController.loadLevel(level)
    }
    fun move(direction: Direction)
    {
        gameController.move(direction)
    }
    fun restart(level: IntArray)
    {
        gameController.restart(level)
    }
    fun setPlayerLocation()
    {
        gameController.setPlayerLocation()
    }
}