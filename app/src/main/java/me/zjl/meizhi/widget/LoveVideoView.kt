package me.zjl.meizhi.widget

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.util.AttributeSet
import android.util.Base64
import android.webkit.*
import me.zjl.meizhi.BuildConfig
import org.jetbrains.anko.doFromSdk

/**
 * Created by chang on 2017-08-27.
 */
class LoveVideoView :WebView {


    lateinit var myContext:Context
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr){
        myContext = context
        webViewClient = LoveClient()
        webChromeClient = Chrome()
        val s = settings
        s.javaScriptEnabled = true
        s.allowFileAccess  =true
        s.databaseEnabled = true
        s.domStorageEnabled = true
        s.saveFormData = true
        s.setAppCacheEnabled(true)
        s.cacheMode = WebSettings.LOAD_DEFAULT
        s.loadWithOverviewMode = false
        s.useWideViewPort =true

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
        }
    }

    private inner class LoveClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView,url:String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String) {
            super.onPageFinished(view, url)
            when {
                url.contains("www.vmovier.com") -> injectCSS("vmovier.css")
                url.contains("video.weibo.com") -> injectCSS("weibo.css")
                url.contains("m.miaopai.com") -> injectCSS("miaopai.css")
            }
        }

        private fun injectCSS(filename: String) = try {
            val inputStream = myContext.assets.open(filename)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
            loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Chrome : WebChromeClient(), MediaPlayer.OnCompletionListener {
        override fun onCompletion(p0: MediaPlayer?) {
            if (p0 != null) {
                if (p0.isPlaying) {
                    p0.stop()
                }
                p0.reset()
                p0.release()
            }
        }
    }

}