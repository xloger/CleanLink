package com.xloger.cleanlink

import android.app.Service
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.content.ClipData
import android.content.ContentResolver


class MonitorService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        val clip = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        clip.addPrimaryClipChangedListener {
            if (!clip.hasPrimaryClip()) {
                log("剪切板无数据")
            }

            if (clip.primaryClipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                val clipData = clip.primaryClip
                val item = clipData.getItemAt(0)
                with(item.text.toString()) {
                    if (UrlParser.findUrl(this)) {
                        val cleanUrl = UrlParser.clear(this)
                        log("处理后的链接：$cleanUrl")
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

    private fun log(text: String) {
        Log.d("xloger", text)
    }
}
