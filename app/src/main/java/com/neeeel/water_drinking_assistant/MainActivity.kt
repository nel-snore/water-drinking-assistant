package com.neeeel.water_drinking_assistant

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.neeeel.water_drinking_assistant.component.Dialog
import com.neeeel.water_drinking_assistant.component.TopBar
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)

        setContent {
            rememberSystemUiController().setStatusBarColor(Transparent, isSystemInDarkTheme())
            WaterdrinkingassistantTheme {
                Content(
                    onStartSettingActivity = {
                        val intent = Intent().apply {
                            action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            putExtra("app_package", packageName)
                            putExtra("app_uid", applicationInfo.uid)
                            putExtra("android.provider.extra.APP_PACKAGE", packageName)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun Content(
    onStartSettingActivity: () -> Unit = {}
) {
    var openDialog      by remember { mutableStateOf(false) }
    var dialogTitle     by remember { mutableStateOf("Title") }
    var dialogContent   by remember { mutableStateOf("Content") }
    var onDialogConfirm by remember { mutableStateOf({}) }

    if (openDialog) {
        Dialog(
            dialogTitle,
            dialogContent,
            onConfirm = {
                openDialog = false
                onDialogConfirm()
                onDialogConfirm = {}
            },
            onDismiss = {
                openDialog = false
                onDialogConfirm = {}
            }
        )
    }

    val list = mutableListOf<String>()
    for (i in 1..100) {
        list.add(i.toString())
    }

    Column {
        // 顶部导航栏
        TopBar(
            // 添加任务
            onClickActionButton = {
                openDialog = true
            },
            // 打开通知权限页面
            onClickNavigationButton = {
                dialogTitle = "允许通知"
                dialogContent = "提醒功能需要开启“允许通知”"
                onDialogConfirm = onStartSettingActivity
                openDialog = true
            }
        )

        // 内容
        Column(modifier = Modifier
            .weight(1.0.toFloat())
            .padding(10.dp, 0.dp)
        ) {
            Card()
        }
    }
}


@Composable
fun Card() {
    Surface(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp),
        tonalElevation = 5.dp
    ) {
        Row {
            Icon(Icons.Filled.Warning, "")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}