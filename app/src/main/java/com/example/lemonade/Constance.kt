package com.example.lemonade

object Constance {
    const val TAG = "MyTag"

    const val LEMONADE_STATE = "LEMONADE_STATE"
    const val LEMON_SIZE = "LEMON_SIZE"
    const val SQUEEZE_COUNT = "SQUEEZE_COUNT"

    // SELECT represents the "pick lemon" state
    const val SELECT = "select"

    // SQUEEZE represents the "squeeze lemon" state
    const val SQUEEZE = "squeeze"

    // DRINK represents the "drink lemonade" state
    const val DRINK = "drink"

    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    const val RESTART = "restart"
}