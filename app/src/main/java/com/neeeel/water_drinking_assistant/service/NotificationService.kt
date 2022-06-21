package com.neeeel.water_drinking_assistant.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.neeeel.water_drinking_assistant.R
import com.neeeel.water_drinking_assistant.database.RoomDb
import com.neeeel.water_drinking_assistant.ui.activity.MainActivity
import java.util.*

class NotificationService : Service() {

    companion object {
        private const val TAG = "NotificationService"
        private const val ONE_MIN = 1000L * 60
        // 渠道 ID 和名称
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "定时提醒"

        // 随机产生一个通知 ID
        private val randomId
            get() = Random().nextInt()
    }

    private val clockList = ClockList()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "服务启动")
        // 创建渠道
        createNotificationChannel()
        // 开始定时任务
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun start() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val notifyList = clockList.addCount()
                clockList.compare(RoomDb.INSTANCE.clockDao().all())

                if (notifyList.isEmpty()) {
                    return
                }
                val sb = StringBuilder()
                notifyList.forEach {
                    sb.append(it).append(";")
                }
                if (notifyList.isNotEmpty()) {
                    sb.deleteCharAt(sb.lastIndex)
                }
                notification(sb.toString())
            }
        }, ONE_MIN, ONE_MIN)
    }

    private fun notification(content: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(randomId, notification)
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_NAME
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}