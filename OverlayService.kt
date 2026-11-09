package com.fcpowershot.panel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

class OverlayService : Service() {
    private var manager: WindowManager? = null
    private var chip: TextView? = null
    private val channelId = "fc_power_shot_overlay"

    override fun onCreate() {
        super.onCreate()
        createNotification()
        startForeground(7, notification())
        manager = getSystemService(WINDOW_SERVICE) as WindowManager
        chip = TextView(this).apply {
            text = "⚽ POWER SHOT"
            textSize = 14f
            setTextColor(Color.WHITE)
            setPadding(24, 16, 24, 16)
            setBackgroundColor(Color.rgb(0, 133, 119))
            elevation = 12f
            setOnClickListener {
                text = if (text.toString().contains("POWER")) "POWER SHOT • HAZIR" else "⚽ POWER SHOT"
            }
            setOnLongClickListener {
                stopSelf()
                true
            }
        }
        val type = if (Build.VERSION.SDK_INT >= 26)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 18; y = 180
        }
        manager?.addView(chip, params)
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "Power Shot panel",
                NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun notification(): Notification {
        return if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, channelId)
                .setContentTitle("FC Power Shot")
                .setContentText("Yüzen yardımcı panel etkin. Kapatmak için panele uzun bas.")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("FC Power Shot")
                .setContentText("Yüzen yardımcı panel etkin.")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
        }
    }

    override fun onDestroy() {
        chip?.let { manager?.removeView(it) }
        chip = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
