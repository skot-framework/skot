package tech.skot.view.extensions

import androidx.core.view.WindowInsetsCompat

fun WindowInsetsCompat.systemBars() = getInsets(WindowInsetsCompat.Type.systemBars())