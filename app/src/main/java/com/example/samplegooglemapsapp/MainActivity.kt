package com.example.samplegooglemapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.samplegooglemapsapp.presentation.checkForPermission
import com.example.samplegooglemapsapp.presentation.screens.LocationPermissionScreen
import com.example.samplegooglemapsapp.presentation.screens.MapScreen
import com.example.samplegooglemapsapp.ui.theme.SampleGoogleMapsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleGoogleMapsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var hasLocationPermission by remember {
                        mutableStateOf(checkForPermission(this))
                    }

                    if (hasLocationPermission) {
                        MapScreen(this)
                    } else {
                        LocationPermissionScreen {
                            hasLocationPermission = true
                        }
                    }
                }
            }
        }
    }
}

