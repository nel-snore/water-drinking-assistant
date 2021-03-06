package com.neeeel.water_drinking_assistant.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neeeel.water_drinking_assistant.database.MyDataStore
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.database.bean.Clock
import com.neeeel.water_drinking_assistant.ui.component.ClockCard
import com.neeeel.water_drinking_assistant.ui.component.Dialog
import com.neeeel.water_drinking_assistant.ui.component.TopBar
import com.neeeel.water_drinking_assistant.ui.theme.WaterdrinkingassistantTheme
import com.neeeel.water_drinking_assistant.viewmodel.MyViewModel
import kotlin.concurrent.thread

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val myViewModel: MyViewModel by viewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        myViewModel.load()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WaterdrinkingassistantTheme {
                Content { i ->
                    launcher.launch(i)
                }
            }
        }
        initList()
    }

    /**
     * ??????????????????????????????????????????
     */
    private fun initList() = thread {
        val initDatabase = MyDataStore.getData("init_database", false)
        if (initDatabase) {
            return@thread
        }
        // ?????????????????????
        val clock = Clock(
            title = "?????????????????????????????????????????????????????????????????????????????????????????????.jpg",
            interval = 30,
            maxLines = 5
        )
        RoomDb.INSTANCE.clockDao().insert(clock)
        MyDataStore.putData("init_database", true)

        myViewModel.load()
    }

    @Composable
    private fun Content(
        onAddNewClock: (intent: Intent) -> Unit = {}
    ) {
        val context = LocalContext.current
        val dao = RoomDb.INSTANCE.clockDao()

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

        Column {
            // ???????????????
            TopBar {
                // ???????????????
                IconButton(onClick = {
                    onAddNewClock(Intent(context, AddNewClockActivity::class.java))
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
                // ????????????????????????
                IconButton(
                    onClick = {
                        dialogTitle = "????????????"
                        dialogContent = "??????????????????????????????????????????"
                        onDialogConfirm = {
                            val intent = Intent().apply {
                                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                                putExtra("app_package", context.packageName)
                                putExtra("app_uid", context.applicationInfo.uid)
                                putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                            }
                            context.startActivity(intent)
                        }
                        openDialog = true
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Localized description"
                    )
                }
            }

            val viewModel: MyViewModel = viewModel()
            viewModel.getClocks().observeAsState().value?.let {
                // ??????
                LazyColumn(modifier = Modifier
                    .weight(1.0.toFloat())
                    .padding(10.dp, 0.dp)
                ) {
                    items(it) { item ->
                        ClockCard(
                            clock = item,
                            onDelete = {
                                thread {
                                    dao.delete(item)
                                    viewModel.load()
                                    "?????????".toast()
                                }
                            },
                            onEdit = {
                                val intent = Intent(context, AddNewClockActivity::class.java).apply {
                                    putExtra("id", item.id)
                                }
                                onAddNewClock(intent)
                            }
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Content()
    }
}