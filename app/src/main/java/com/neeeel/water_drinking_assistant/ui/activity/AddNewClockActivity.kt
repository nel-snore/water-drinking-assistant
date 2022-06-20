package com.neeeel.water_drinking_assistant.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import com.neeeel.water_drinking_assistant.ui.component.Dialog
import com.neeeel.water_drinking_assistant.ui.component.TopBar
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme
import com.neeeel.water_drinking_assistant.viewmodel.MyViewModel
import java.lang.NumberFormatException
import kotlin.concurrent.thread

class AddNewClockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterdrinkingassistantTheme {
                Content(onFinish = { finish() })
            }
        }
    }
}

@Composable
private fun Content(
    onFinish: () -> Unit = {}
) {
    var titleText   by remember { mutableStateOf("") }
    var timeText    by remember { mutableStateOf("") }
    var timeValueIsError by remember { mutableStateOf(false) }

    var openDialog      by remember { mutableStateOf(false) }
    var dialogTitle     by remember { mutableStateOf("Title") }
    var dialogContent   by remember { mutableStateOf("Content") }
    var onDialogConfirm by remember { mutableStateOf({}) }

    if (openDialog) {
        Dialog(
            dialogTitle,
            dialogContent,
            onDismiss = { openDialog = false },
            onConfirm = {
                openDialog = false
                onDialogConfirm()
            },
        )
    }

    Column {
        // 顶部导航栏
        TopBar()

        Surface(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(10.dp),
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
        ) {


            Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = titleText,
                    onValueChange = {
                        if (it.length <= 50) {
                            titleText = it
                        }
                    },
                    label = { Text("提醒内容") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        if (titleText.isNotEmpty()) {
                            IconButton(onClick = { titleText = "" }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "清空")
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = timeText,
                    isError = timeValueIsError,
                    onValueChange = {
                        if (it.length <= 3) {
                            timeText = it
                        }
                        timeValueIsError = !Regex("\\d+").matches(it)
                    },
                    label = { Text("提醒间隔（单位分）") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    trailingIcon = {
                        if (timeText.isNotEmpty()) {
                            IconButton(onClick = { timeText = "" }) {
                                Icon(imageVector = Icons.Filled.Clear, contentDescription = "清空")
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                fun String.showDialog(onConfirm: () -> Unit = {}) {
                    dialogTitle = "提示"
                    dialogContent = this
                    onDialogConfirm = onConfirm
                    openDialog = true
                }

                ElevatedButton(
                    onClick = {
                        if (titleText.isEmpty()) {
                            "提醒内容不能为空".showDialog()
                            return@ElevatedButton
                        }
                        if (timeText.isEmpty()) {
                            "提醒间隔不能为空".showDialog()
                            return@ElevatedButton
                        }
                        val interval = try {
                            timeText.toInt()
                        } catch (e: NumberFormatException) {
                            -1
                        }
                        if (interval <= 0) {
                            "提醒间隔输入有误".showDialog()
                            return@ElevatedButton
                        }

                        thread {
                            val clock = Clock(
                                title = titleText,
                                interval = interval
                            )
                            RoomDb.INSTANCE.clockDao().insert(clock)
                            "已添加".showDialog { onFinish() }
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.elevatedButtonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 1.dp,
                        focusedElevation = 3.dp,
                        hoveredElevation = 3.dp,
                        disabledElevation = 1.dp,
                    )
                ) {
                    Text(text = "添加", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Content()
}