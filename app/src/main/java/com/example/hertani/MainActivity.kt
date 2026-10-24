package com.example.hertani

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hertani.ui.HertaniApp
import com.example.hertani.ui.theme.HertaniTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HertaniTheme {
                HertaniApp()
            }
        }
    }
}