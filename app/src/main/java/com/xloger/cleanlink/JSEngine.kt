package com.xloger.cleanlink

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.eclipsesource.v8.V8Array




/**
 * Created on 2018/11/13 11:53.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object JSEngine {
    lateinit var handler : V8Object
    private val runtime = V8.createV8Runtime()

    fun init() {
        val code = "var handler = {clipboardText: function (text) {return '1' + text}}"
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