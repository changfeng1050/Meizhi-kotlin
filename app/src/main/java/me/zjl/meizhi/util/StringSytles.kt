package me.zjl.meizhi.util

import android.content.Context
import android.text.SpannableString
import android.text.style.TextAppearanceSpan

/**
 * Created by chang on 2017-08-27.
 */
class StringStyles {

    companion object {
        fun format(context: Context, text: String, style: Int): SpannableString {
            val spannableString = SpannableString(text)
            spannableString.setSpan(TextAppearanceSpan(context, style), 0, text.length, 0)
            return spannableString
        }
    }
}