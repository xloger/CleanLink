package com.xloger.cleanlink

import android.app.Service
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.content.ClipData
import android.content.ContentResolver
import org.jetbrains.anko.ctx


class MonitorService : Service() {
    var lastResult = ""
    val notificationId = 9527
    lateinit var xNotification: XNotification
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        xNotification = XNotification(ctx)
                .create("CleanLink 服务启动", "", R.mipmap.ic_launcher)
                .setId(notificationId)
        startForeground(notificationId, xNotification.builder.build())
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        JSEngine.init(ctx)
        val clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        clip.addPrimaryClipChangedListener {
            if (!clip.hasPrimaryClip()) {
                log("剪切板无数据")
            }

            if (clip.primaryClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val clipData = clip.primaryClip
                val item = clipData.getItemAt(0)
                with(item.text.toString()) {
                    if (lastResult == this) {
                        return@with
                    }
                    //                    if (UrlParser.findUrl(this)) {
//                        val cleanUrl = UrlParser.clear(this)
//                        log("处理后的链接：$cleanUrl")
//                    }
                    val result = JSEngine.parseLink(this)
                    if (result != this) {
                        log("之前的链接：$this")
                        log("处理之后的链接：$result")
                        lastResult = result
                        paste(result)
                    } else {
                        log("相同，不粘贴")
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

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }


    private fun paste(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textCd = ClipData.newPlainText("link", text)
        clipboard.primaryClip = textCd
        notifyForOpen(text)
    }

    private fun notifyForOpen(text: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(text)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        xNotification
                .setResultIntent(intent)
                .updateText("处理链接：$text", "点击即可在浏览器中打开")
    }


    private fun log(text: String) {
        Log.d("xloger", text)
    }
}
