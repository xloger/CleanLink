package com.xloger.cleanlink

import android.net.Uri
import android.util.Log

/**
 * Created on 2018/5/4 21:48.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object UrlParser {

    /**
     * 从一个字符串中找寻并返回一个链接
     */
    fun findUrl(s: String): String {
        var ret = ""
        log("要判断的文字是：$s")
        if (s.contains("http")) {
            ret = s
            return ret
        }
        return ret
    }

    /**
     * 处理链接 utm 信息
     */
    fun clear(s: String): Uri {
        val uri = Uri.parse(s)
        if (uri != null) {
//            Log.d("xloger", "${uri.scheme} ${uri.host} ${uri.path}")
            val builder = uri.buildUpon()
            uri.queryParameterNames.forEach {
                log("key: $it")
                log("value: ${uri.getQueryParameter(it)}")

            }

            builder.clearQuery()
            return builder.build()

        } else {
            log("不是链接")
        }

        return uri
    }

    private fun log(text: String) {
        Log.d("xloger", text)
    }
}