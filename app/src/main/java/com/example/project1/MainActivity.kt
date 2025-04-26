package com.example.project1


import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Build
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.text.font.FontVariation
import com.example.project1.presentation.ui.CharactersScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContent {
                CharactersScreen()
            }
    }
}

