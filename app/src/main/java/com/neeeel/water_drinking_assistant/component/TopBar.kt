package com.neeeel.water_drinking_assistant.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    onOpenSetting: () -> Unit = {},
    onAddClock: () -> Unit = {},
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp))

        SmallTopAppBar(
            title = { Text("Assistant") },
            actions = {
                IconButton(onClick = onAddClock) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
                IconButton(onClick = onOpenSetting) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Localized description"
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            )
        )
    }
}