package com.xloger.cleanlink

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_service.setOnClickListener {
            startService(Intent(baseContext, MonitorService::class.java))
        }
        stop_service.setOnClickListener {
            stopService(Intent(baseContext, MonitorService::class.java))
        }
        copy_text.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val textCd = ClipData.newPlainText("test", copy_text.text)
            clipboard.setPrimaryClip(textCd)
        }
    }
}
