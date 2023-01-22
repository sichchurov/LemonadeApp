/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.lemonade.Constance.DRINK
import com.example.lemonade.Constance.LEMONADE_STATE
import com.example.lemonade.Constance.LEMON_SIZE
import com.example.lemonade.Constance.RESTART
import com.example.lemonade.Constance.SELECT
import com.example.lemonade.Constance.SQUEEZE
import com.example.lemonade.Constance.SQUEEZE_COUNT
import com.example.lemonade.Constance.TAG
import com.example.lemonade.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Default the state to select
    private var lemonadeState = "select"

    // Default lemonSize value
    private var lemonSize = 0

    // Default the squeezeCount value
    private var squeezeCount: Int = 0

    private var lemonTree = LemonTree()
    private lateinit var lemonImage: ImageView
    private lateinit var squeezeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            lemonadeState = it.getString(LEMONADE_STATE, "select")
            lemonSize = it.getInt(LEMON_SIZE, 0)
            squeezeCount = it.getInt(SQUEEZE_COUNT, 0)
        }

        lemonImage = binding.imageLemonState

        lemonImage.setOnClickListener {
            clickLemonImage()
            setViewElements()
        }
        lemonImage.setOnLongClickListener {
            squeezeCount += 1
            lemonSize -= 1
            showSnackbar()
        }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        squeezeText = binding.twSqueezeCount
        when (lemonadeState) {
            SELECT -> {
                lemonImage.setImageResource(R.drawable.lemon_squeeze)
                lemonSize = lemonTree.pick()
                lemonadeState = SQUEEZE
            }
            SQUEEZE -> {
                if (lemonSize != 0) {
                    Log.d(TAG, "LEMON SIZE MIDDLE: $lemonSize")
                    squeezeText.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.squeeze_count_left, lemonSize)
                    }
                } else {
                    lemonImage.setImageResource(R.drawable.lemon_drink)
                    lemonadeState = DRINK
                    squeezeText.visibility = View.INVISIBLE
                    squeezeCount = 0
                }

                Log.d(TAG, "Lemon size $lemonSize")
                Log.d(TAG, "SQUEEZE COUNT: $squeezeCount")

            }
            DRINK -> {
                lemonImage.setImageResource(R.drawable.lemon_restart)
                lemonadeState = RESTART
            }
            RESTART -> {
                lemonImage.setImageResource(R.drawable.lemon_tree)
                lemonadeState = SELECT
            }
        }
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = binding.textAction
        textAction.text = getString(
            when (lemonadeState) {
                SELECT -> R.string.lemon_select
                SQUEEZE -> R.string.lemon_squeeze
                DRINK -> R.string.lemon_drink
                else -> R.string.lemon_empty_glass
            }
        )
    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            binding.constraintLayout,
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
