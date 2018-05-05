package com.xloger.cleanlink

import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.content.ClipData
import android.content.ContentResolver
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


class MonitorService : Service() {
    private var clip by Delegates.notNull<ClipboardManager>()

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        showNotification()

        clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        clip.addPrimaryClipChangedListener {
            if (!clip.hasPrimaryClip()) {
                log("剪切板无数据")
            }

            if (clip.primaryClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val clipData = clip.primaryClip
                val item = clipData.getItemAt(0)
                with(UrlParser.findUrl(item.text.toString())) {
                    if (isNotBlank()) {
                        val cleanUrl = UrlParser.clear(this)
                        log("处理后的链接：$cleanUrl")
                        if (cleanUrl.toString() != this) {
                            dealUrl(cleanUrl)
                        }
                    }
                }
            }

//            } else if (clip.primaryClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) {
//                val cr = contentResolver
//                val cdUri = clip.primaryClip
//                val item = cdUri.getItemAt(0)
//                val uri = item.uri
//                if (uri != null) {
//                    val mimeType = cr.getType(uri)
//                    if (mimeType != null) {
////                        if (mimeType == MIME_TYPE_CONTACT) {
//                            val pasteCursor = cr.query(uri, null, null, null, null)
//                            if (pasteCursor != null) {
//                                if (pasteCursor.moveToFirst()) {
//                                    //此处对数据进行操作就可以了,前提是有权限
//                                }
//                            }
//                            pasteCursor.close()
////                        }
//                    }
//                }
//            }
        }

    }

    /**
     * 处理链接，将其复制到剪切板
     */
    private fun dealUrl(uri: Uri) {
        val textCd = ClipData.newPlainText("clean", uri.toString())
        clip.primaryClip = textCd
    }

    override fun onDestroy() {
        super.onDestroy()
        stopNotification()
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle("我还活着")
                .setContentText("=。=")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
        notificationManager.notify(10086, builder.build())

    }

    private fun stopNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(10086)
    }

    private fun log(text: String) {
        Log.d("xloger", text)
    }
}
