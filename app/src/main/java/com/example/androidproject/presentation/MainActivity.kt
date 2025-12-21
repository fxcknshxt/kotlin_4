package com.example.androidproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidproject.presentation.theme.AndroidProjectTheme
import com.example.androidproject.presentation.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidProjectTheme {
                AppNavigation()
            }
        }
    }
}
