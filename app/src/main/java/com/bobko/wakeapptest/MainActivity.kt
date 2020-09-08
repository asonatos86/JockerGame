package com.bobko.wakeapptest

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var playerView : ImageView

    private var startX: Float = 0.0f
    private var startY: Float = 0.0f

    private lateinit var viewModel: BaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)

        playerView.setOnClickListener(View.OnClickListener {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.root, GameOverFragment.newInstance())
                .addToBackStack(null)
                .commit()
        })

        viewModel = ViewModelProvider(this)[BaseViewModel::class.java]

        viewModel.setGameController(GameController(this, gameField, playerView))

        viewModel.retry.observe(this, Observer {
            it?.let {
                supportFragmentManager.popBackStackImmediate()
                viewModel.restart(resources.getIntArray(R.array.lvl1))
            }
        })

        viewModel.gameOver.observe(this, Observer {
            it?.let {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root, GameOverFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        })
        viewModel.loadLevel(resources.getIntArray(R.array.lvl1))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_UP ->
            {
                viewModel.move(Direction(startX,startY,event.x,event.y))
            }
        }
       return false;
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        viewModel.setPlayerLocation()
    }
}