package com.xloger.cleanlink

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import org.jetbrains.anko.bundleOf
import java.util.*

/**
 * Created on 2018/11/14 15:03.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class XNotification(val context: Context) {
    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var id = (Math.random()*1000).toInt()
    lateinit var builder: NotificationCompat.Builder

    val channelId = "huwo"
    val channelName = "push"

    fun create(title: String, content: String, @DrawableRes iconId: Int): XNotification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context, channelId)
        } else {
            builder = NotificationCompat.Builder(context)
        }
        builder
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)

        return this
    }

    fun show() : XNotification {
        manager.notify(id, builder.build())
        return this
    }

    fun setId(id: Int) : XNotification {
        this.id = id
        return this
    }

    fun updateText(title: String = "", content: String = "") : XNotification {
        if (::builder.isInitialized) {
            builder.setContentTitle(title)
            builder.setContentText(content)
            manager.notify(id, builder.build())
        }
        return this
    }

    fun updateProgress(current: Int, max: Int): XNotification {
        if (::builder.isInitialized) {
            builder.setProgress(max, current, false)
            manager.notify(id, builder.build())
        }

        return this
    }

    fun setResultIntent(intent: Intent) : XNotification {
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        return this
    }

    inline fun <reified T : Activity> setResultActivity(vararg params: Pair<String, Any>) : XNotification {
        val resultIntent = Intent(context, T::class.java)
        //此处 Anko 采用了枚举解决该问题：https://stackoverflow.com/questions/42485596/universal-pair-value-type-for-intent-extras
        resultIntent.putExtras(bundleOf(*params))
        val intent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(intent)
        return this
    }
}