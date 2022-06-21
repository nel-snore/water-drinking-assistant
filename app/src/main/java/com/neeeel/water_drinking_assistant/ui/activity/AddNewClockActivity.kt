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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import com.neeeel.water_drinking_assistant.ui.component.Dialog
import com.neeeel.water_drinking_assistant.ui.component.TopBar
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme
import kotlin.concurrent.thread

class AddNewClockActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getLongExtra("id", -1)
        val clock = if (id <= 0) {
            Clock(title = "提醒", interval = 30)
        } else {
            RoomDb.INSTANCE.clockDao().queryById(id)
        }

        setContent {
            WaterdrinkingassistantTheme {
                Content(clock) {
                    finish()
                }
            }
        }
    }

    @Composable
    private fun Content(
        clock: Clock = Clock(),
        onFinish: () -> Unit = {}
    ) {
        var titleText   by remember { mutableStateOf("") }
        var timeText    by remember { mutableStateOf("") }
        var timeValueIsError by remember { mutableStateOf(false) }

        var openDialog      by remember { mutableStateOf(false) }
        var dialogTitle     by remember { mutableStateOf("Title") }
        var dialogContent   by remember { mutableStateOf("Content") }
        var onDialogConfirm by remember { mutableStateOf({}) }

        val dao = RoomDb.INSTANCE.clockDao()

        titleText = clock.title
        timeText = clock.interval.toString()

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
                    modifier = Modifier.padding(20.dp),
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

                    Spacer(modifier = Modifier.height(20.dp))

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

                    Spacer(modifier = Modifier.height(20.dp))

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
                                clock.title = titleText.trim()
                                clock.interval = interval

                                if (clock.id == null) {
                                    dao.insert(clock)
                                    "已添加".toast()
                                    onFinish()
                                } else {
                                    dao.update(clock)
                                    "已更新".toast()
                                    onFinish()
                                }
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
                        Text(
                            text = if (clock.id == null) { "添加" } else { "更新" },
                            fontWeight = FontWeight.Bold
                        )
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
}

