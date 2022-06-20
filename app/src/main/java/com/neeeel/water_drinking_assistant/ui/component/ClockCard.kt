package com.neeeel.water_drinking_assistant.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.neeeel.water_drinking_assistant.database.bean.Clock

@Composable
fun ClockCard(
    clock: Clock,
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(0.dp, 10.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                .weight(1.0.toFloat())
                .fillMaxHeight()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                    )
                    Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(
                            text = clock.title,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(start = 10.dp),
                            maxLines = clock.maxLines,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                Box(modifier = Modifier.padding(top = 5.dp)) {
                    Text(
                        text = "${clock.interval} Min",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.displayMedium,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Box(modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
            ) {
                IconButton(
                    onClick = {expanded = true},
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "actions",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }

                // 菜单
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset((-70).dp, (-30).dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("编辑") },
                        onClick = {
                            expanded = false
                            onEdit()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("删除") },
                        onClick = {
                            expanded = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}