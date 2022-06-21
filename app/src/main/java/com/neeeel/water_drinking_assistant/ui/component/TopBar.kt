package com.neeeel.water_drinking_assistant.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.neeeel.water_drinking_assistant.R

@Composable
fun TopBar(
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp))

        SmallTopAppBar(
            title = { Text(stringResource(id = R.string.app_name)) },
            actions = actions,
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondary,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
            )
        )
    }
}