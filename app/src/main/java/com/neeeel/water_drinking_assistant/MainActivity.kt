package com.neeeel.water_drinking_assistant

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.neeeel.water_drinking_assistant.component.ClockCard
import com.neeeel.water_drinking_assistant.component.Dialog
import com.neeeel.water_drinking_assistant.component.TopBar
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)

        setContent {
            rememberSystemUiController().setStatusBarColor(Transparent, false)
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

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        // 顶部导航栏
        TopBar(
            // 打开通知权限页面
            onOpenSetting = {
                dialogTitle = "允许通知"
                dialogContent = "提醒功能需要开启“允许通知”"
                onDialogConfirm = onStartSettingActivity
                openDialog = true
            },
            onAddClock = {

            }
        )

        // 内容
        Column(modifier = Modifier
            .weight(1.0.toFloat())
            .padding(10.dp, 0.dp)
        ) {
            ClockCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Content()
}