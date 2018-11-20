package com.xloger.cleanlink

import android.content.Context
import android.os.Environment
import android.util.Log
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.eclipsesource.v8.V8Array
import org.mozilla.javascript.xml.XMLLib
import java.io.File


/**
 * Created on 2018/11/13 11:53.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object JSEngine {
    lateinit var handler : V8Object
    private val runtime = V8.createV8Runtime()

    fun init(context: Context) {
        var text = context.assets.open("app.js").bufferedReader().readText()
        val file = File(context.getExternalFilesDir("js"), "app.js")
        val temp = File(context.getExternalFilesDir("js"), "temp")
        if (!temp.exists()) {
            temp.createNewFile()
        }
        if (file.exists()){
            text = file.readText()
        } else {
            Log.d("debug", "${file.path} 不存在")
        }
//        val code = "var handler = {clipboardText: function (text) {return '1' + text}}"
//        Log.d("debug", text)
        val code = text
        runtime.executeVoidScript(code)
        handler = runtime.getObject("handler")
    }

    fun parseLink(link: String) : String {
        val parameters = V8Array(runtime).push(link)
        val updatedText = handler.executeStringFunction("clipboardText", parameters)
        parameters.release()
        return updatedText
    }

    fun destory() {
        handler.release()
        runtime.release()
    }
}