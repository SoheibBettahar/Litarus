package com.example.guttenburg.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.guttenburg.ui.viewmodels.TrainingViewModel


private const val TAG = "TrainingScreen"

@Composable
fun TrainingScreen(viewModel: TrainingViewModel = hiltViewModel()) {

    val storagePermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                viewModel.onPermissionResult(
                    permission = Manifest.permission.WRITE_EXTERNAL_STORAGE, isGranted = isGranted
                )
            })


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            storagePermissionResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }) {
            Text(text = "Request Permission")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { }) {
            Text(text = "Request Multiple Permissions")
        }
    }

}




