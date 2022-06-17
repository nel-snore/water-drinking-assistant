package com.neeeel.water_drinking_assistant.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.neeeel.water_drinking_assistant.database.MyDataStore.INIT
import com.neeeel.water_drinking_assistant.ui.component.ClockCard
import com.neeeel.water_drinking_assistant.ui.component.Dialog
import com.neeeel.water_drinking_assistant.ui.component.TopBar
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import com.neeeel.water_drinking_assistant.database.dataStore
import com.neeeel.water_drinking_assistant.database.getInt
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme
import com.neeeel.water_drinking_assistant.viewmodel.MyViewModel
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

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

        runBlocking {
            dataStore.getInt(INIT).collect {

            }
        }


        val viewModel: MyViewModel by viewModels()
        viewModel.getClocks().observe(this) {
            Log.d(TAG, "onCreate: $it")
        }
        viewModel.load()
    }


    private fun initClockList() {
        val dao = RoomDb.INSTANCE.clockDao()

        if (dao.count() <= 0) {
            dao.insert(Clock(title = "提醒喝水小助手", interval = 30))
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