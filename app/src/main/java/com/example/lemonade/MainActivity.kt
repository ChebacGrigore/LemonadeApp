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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /**
     * DO NOT ALTER ANY VARIABLE OR VALUE NAMES OR THEIR INITIAL VALUES.
     *
     * Anything labeled var instead of val is expected to be changed in the functions but DO NOT
     * alter their initial values declared here, this could cause the app to not function properly.
     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
            setViewElements()
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
            false
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    private fun stateLemonade(
        lemonadeState: String = SQUEEZE,
        lemonSize: Int = lemonTree.pick(),
        squeezeCount: Int = 0
    ) {
       this.lemonadeState = lemonadeState
       this.lemonSize = lemonSize
       this.squeezeCount = squeezeCount
    }

    // observe state  of application
    private fun clickLemonImage() {

        when (lemonadeState) {
            SELECT -> {
                stateLemonade()
            }
            SQUEEZE -> {
                squeezeCount++;
                lemonSize--;
                if (lemonSize == 0) {
                    stateLemonade(DRINK, -1)
                }
            }
            DRINK -> {
                stateLemonade(RESTART)
            }
            else -> { stateLemonade(SELECT) }
        }
    }

    // change text and image on phone app
    private fun updateDataLemonade(textViewMessage: String, idImageResource: Int) {
        val textAction= findViewById<TextView>(R.id.text_action)
        val imageView = findViewById<ImageView>(R.id.image_lemon_state)

        textAction.text = textViewMessage
        imageView.setImageResource(idImageResource)
    }


    // check step of lemonade and chang data based on state of app
    private fun setViewElements() {

        when (lemonadeState) {
            SELECT -> {
                updateDataLemonade(getString(R.string.lemon_select),R.drawable.lemon_tree)
            }
            SQUEEZE -> {
                Toast.makeText(
                    this,
                    "{ How many clicks = $lemonSize do you need to do a lemonade}",
                    Toast.LENGTH_SHORT
                ).show()
                updateDataLemonade(getString(R.string.lemon_select),R.drawable.lemon_tree)
            }
            DRINK -> {
                updateDataLemonade(getString(R.string.lemon_drink),R.drawable.lemon_drink)
            }
            else -> {
                updateDataLemonade(getString(R.string.lemon_empty_glass),R.drawable.lemon_restart)
            }
        }
    }

    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
