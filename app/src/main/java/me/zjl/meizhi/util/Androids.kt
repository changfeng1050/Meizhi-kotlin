package me.zjl.meizhi.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

/**
 * Created by chang on 2017-09-07.
 */
class Androids {
    companion object {
        fun copyToClipBoard(context: Context, text: String, success: String) {
            val clipData = ClipData.newPlainText("meizhi_copy", text)
            val manager = context.getSystemService(
                    Context.CLIPBOARD_SERVICE) as ClipboardManager
            manager.primaryClip = clipData
            Toast.makeText(context, success, Toast.LENGTH_SHORT).show()
        }
    }
}