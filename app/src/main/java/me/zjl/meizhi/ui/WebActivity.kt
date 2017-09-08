package me.zjl.meizhi.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import android.widget.TextSwitcher
import android.widget.TextView
import me.zjl.meizhi.R
import me.zjl.meizhi.ui.base.ToolbarActivity
import me.zjl.meizhi.util.Androids
import org.jetbrains.anko.*

/**
 * Created by chang on 2017-09-07.
 */
class WebActivity : ToolbarActivity() {

    companion object {
        private val EXTRA_URL = "extra_url"
        private val EXTRA_TITLE = "extra_title"

        fun newIntent(context: Context, extraUrl: String, extraTitle: String): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(EXTRA_URL, extraUrl)
            intent.putExtra(EXTRA_TITLE, extraTitle)
            return intent
        }
    }

    lateinit var webView: WebView
    lateinit var progressbar: ProgressBar
    lateinit var textSwitcher: TextSwitcher

    private lateinit var url: String
    private var title: String? = null

    override fun provideContentViewId() = R.layout.activity_web

    override fun canBack(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = intent.getStringExtra(EXTRA_URL)
        title = intent.getStringExtra(EXTRA_TITLE)

        progressbar = find(R.id.progressbar)
        webView = find(R.id.webView)
        textSwitcher = find(R.id.title)

        with(webView.settings) {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            setAppCacheEnabled(true)
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            setSupportZoom(true)
        }
        webView.webChromeClient = ChromeClient()

        webView.loadUrl(url)

        textSwitcher.setFactory {
            TextView(this@WebActivity).apply {
                setTextAppearance(this@WebActivity, R.style.WebTitle)
                singleLine = true
                ellipsize = TextUtils.TruncateAt.MARQUEE
                postDelayed({
                    isSelected = true
                }, 1738)
            }
        }
        textSwitcher.setInAnimation(this, android.R.anim.fade_in)
        textSwitcher.setOutAnimation(this, android.R.anim.fade_out)
        if (title != null) {
            setTitle(title)
        }
    }

    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
        textSwitcher.setText(title)
    }

    private fun refesh() {
        webView.reload()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_refresh -> {
                refesh()
            }
            R.id.action_copy_url -> {
                val copyDone = getString(R.string.tip_copy_done)
                Androids.copyToClipBoard(this, webView.url, copyDone)
                return true
            }
            R.id.action_open_url -> {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val uri = Uri.parse(url)
                intent.data = uri
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    longToast(R.string.tip_open_fail)
                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    private inner class ChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressbar.progress = newProgress
            if (newProgress == 100) {
                progressbar.visibility = View.GONE
            } else {
                progressbar.visibility = View.VISIBLE
            }
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            setTitle(title)
        }


    }

}