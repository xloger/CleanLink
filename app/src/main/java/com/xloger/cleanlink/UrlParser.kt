package com.xloger.cleanlink

import android.net.Uri
import android.util.Log

/**
 * Created on 2018/5/4 21:48.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object UrlParser {

    fun findUrl(s: String) : Boolean {
        log("要判断的文字是：$s")
        if (s.contains("http")) {
            return true
        }
        return false
    }

    fun clear(s: String) : Uri {
        val uri = Uri.parse(s)
        if (uri != null) {
            Log.d("xloger", "${uri.scheme} ${uri.host} ${uri.path}")
            uri.queryParameterNames.forEach {
                log(it)
                log("值 ${uri.getQueryParameter(it)}")
            }
            
        } else {
            Log.d("xloger", "不是链接")
        }

        return uri
    }

    private fun log(text: String) {
        Log.d("xloger", text)
    }
}