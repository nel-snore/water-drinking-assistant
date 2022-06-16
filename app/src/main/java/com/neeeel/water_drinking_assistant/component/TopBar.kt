package com.neeeel.water_drinking_assistant.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    onClickActionButton: () -> Unit = {},
    onClickNavigationButton: () -> Unit = {}
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp))

        SmallTopAppBar(
            title = { Text("Assistant") },
            navigationIcon = {
                IconButton(onClick = onClickNavigationButton) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(onClick = onClickActionButton) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.primaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )
    }
}