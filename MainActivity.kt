package com.fcpowershot.panel

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val teal = Color.rgb(0, 133, 119)
    private val gamePackage = "com.ea.gp.fifamobile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(0, 91, 82)
        window.navigationBarColor = Color.BLACK
        showLogin()
    }

    private fun root(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(Color.BLACK)
        setPadding(dp(22), 0, dp(22), dp(24))
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
        }

    private fun button(value: String, color: Int, action: () -> Unit): Button =
        Button(this).apply {
            text = value
            setTextColor(Color.WHITE)
            setBackgroundColor(color)
            isAllCaps = false
            textSize = 18f
            setOnClickListener { action() }
        }

    private fun showLogin() {
        val layout = root()
        val header = TextView(this).apply {
            text = "FC POWER SHOT"
            textSize = 23f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), 0, 0, 0)
            setBackgroundColor(teal)
        }
        layout.addView(header, LinearLayout.LayoutParams(-1, dp(68)).apply {
            leftMargin = -dp(22); rightMargin = -dp(22)
        })

        val brand = text("⚽\nFC POWER SHOT", 30f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
        }
        layout.addView(brand, LinearLayout.LayoutParams(-1, dp(300)))

        layout.addView(text("Activation Code", 16f, Color.GRAY))
        val key = EditText(this).apply {
            hint = "Anahtar giriniz (Örn: FC-1D)"
            setHintTextColor(Color.DKGRAY)
            setTextColor(Color.WHITE)
            textSize = 19f
            singleLine = true
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.GRAY))
        }
        layout.addView(key, LinearLayout.LayoutParams(-1, dp(62)).apply {
            topMargin = dp(8); bottomMargin = dp(24)
        })
        layout.addView(button("LOGIN", Color.RED) {
            if (key.text.toString().trim() == "FC-1D") showHome()
            else Toast.makeText(this, "Geçersiz aktivasyon anahtarı", Toast.LENGTH_SHORT).show()
        }, LinearLayout.LayoutParams(-1, dp(58)))
        setContentView(layout)
    }

    private fun showHome() {
        val layout = root()
        val header = TextView(this).apply {
            text = "FC POWER SHOT"
            textSize = 23f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), 0, 0, 0)
            setBackgroundColor(teal)
        }
        layout.addView(header, LinearLayout.LayoutParams(-1, dp(68)).apply {
            leftMargin = -dp(22); rightMargin = -dp(22)
        })
        layout.addView(text("FC POWER SHOT", 27f, Color.WHITE, true).apply {
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(-1, dp(240)))
        layout.addView(button("ÜZERİNDE GÖSTERME İZNİNİ AÇ", teal) {
            if (!Settings.canDrawOverlays(this)) {
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")))
            } else {
                Toast.makeText(this, "İzin zaten açık", Toast.LENGTH_SHORT).show()
            }
        }, LinearLayout.LayoutParams(-1, dp(54)))
        layout.addView(button("BAŞLAT", Color.RED) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "Önce üzerinde gösterme iznini aç", Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")))
                return@button
            }
            val launch = packageManager.getLaunchIntentForPackage(gamePackage)
            if (launch == null) {
                Toast.makeText(this, "FC Mobile bulunamadı. Oyunun yüklü olduğundan emin ol.", Toast.LENGTH_LONG).show()
                return@button
            }
            val service = Intent(this, OverlayService::class.java)
            if (Build.VERSION.SDK_INT >= 26) startForegroundService(service) else startService(service)
            startActivity(launch)
        }, LinearLayout.LayoutParams(-1, dp(58)).apply { topMargin = dp(18) })
        layout.addView(text("Power Shot yardımcı kısayolu. Oyun dosyalarına müdahale etmez.",
            12f, Color.GRAY).apply { gravity = Gravity.CENTER }, LinearLayout.LayoutParams(-1, dp(58)))
        setContentView(layout)
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
