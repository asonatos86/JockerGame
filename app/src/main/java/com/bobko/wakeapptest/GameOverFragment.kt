package com.bobko.wakeapptest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bobko.wakeapptest.databinding.FragmentGameOverBinding
import com.bumptech.glide.Glide


class GameOverFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameOverBinding.bind(inflater.inflate(R.layout.fragment_game_over, container, false))

        viewModel = activity?.run {
            ViewModelProviders.of(this)[BaseViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        binding.model = viewModel
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GameOverFragment().apply {
            }
    }
}