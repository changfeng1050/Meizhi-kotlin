package me.zjl.meizhi.ui

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import me.zjl.meizhi.BuildConfig
import me.zjl.meizhi.R
import me.zjl.meizhi.ui.base.BaseActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.share

/**
 * Created by chang on 2017-08-17.
 */
class AboutActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    lateinit var versionTextView: TextView
    lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_about)

        toolbar = find(R.id.toolbar)
        versionTextView = find(R.id.version)
        collapsingToolbarLayout = find(R.id.collapsing_toolbar)

        setupVersionName()

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            (this@AboutActivity).onBackPressed()
        }
    }

    private fun setupVersionName() {
        versionTextView.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.home -> {
                finish()
                return true
            }

            R.id.menu_share -> {
                share(getString(R.string.share_text))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}